package com.adproject.service.mongo;

import com.adproject.db.datasource.entity.mongo.*;
import com.adproject.utils.ExecutionTime;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class MongoServiceImpl {
    @Autowired  MongoClient mongoClient;
    @Autowired  MongoTemplate mongoTemplate;
    MongoDatabase mongoDB;

    @PostConstruct
    public void postConstruct() {
        mongoDB = mongoClient.getDatabase("ad_db");
    }

    //1. Find data for a given student together with address, city and country.
    //Steps: find student, find city, find country
    public ExecutionTime executeQueryNo1() {
        long startTime = System.currentTimeMillis();
            Query studentID = new Query().addCriteria(where("firstname").is("pAU9rt9"));
            Student student = mongoTemplate.findOne(studentID, Student.class);
            Integer oid = student.address.oid;
            Query cityID = new Query().addCriteria(where("oid").is(oid));
            City city = mongoTemplate.findOne(cityID, City.class);
            Integer tid = city.tid;
            Query countryID = new Query().addCriteria(where("id").is(tid));
            Country country = mongoTemplate.findOne(countryID, Country.class);
        long endTime = System.currentTimeMillis();
        long javaExecutionTime = endTime - startTime;

        //Profile database
        Integer studentTime = timeOfFilteredQuery("student", "firstname","pAU9rt9" );
        Integer cityTime = timeOfFilteredQuery("city", "oid", oid);
        Integer  countryTime = timeOfFilteredQuery("country", "_id", tid);
        long dbExecutionTime = studentTime + cityTime + countryTime;
        return new ExecutionTime(javaExecutionTime, dbExecutionTime);
    }

    //2. Get all teachers of a student
    //Find student, get all faculties in which the student is present, sum up all teachers of the student from all faculties and fetch
    public ExecutionTime executeQueryNo2() {
        long startTime = System.currentTimeMillis();
            Query studentID = new Query().addCriteria(where("firstname").is("pAU9rt9"));
            Student student = mongoTemplate.findOne(studentID, Student.class);
            Query studentWithID = new Query().addCriteria(where("students").is(student.id));
            List<Faculty> faculties = mongoTemplate.find(studentWithID, Faculty.class);
            Set<Integer> teachersID = faculties.stream()
                    .flatMap(it -> it.teachers.stream())
                    .collect(toSet());
            List<Teacher> studentTeachers = teachersID.stream()
                    .map(id -> mongoTemplate.
                    findById(id, Teacher.class)).collect(toList());
        long endTime = System.currentTimeMillis();
        long javaExecutionTime = endTime - startTime;

        Integer studentTime = timeOfFilteredQuery("student", "firstname","pAU9rt9" );
        Integer facultiesTime = timeOfFilteredQuery("faculty", "students", student.id);
        Integer teachersTime = teachersID.stream()
                .mapToInt(id -> timeOfFilteredQuery("teacher", "_id", id))
                .sum();
        long dbExecutionTime = studentTime + facultiesTime + teachersTime;
        return new ExecutionTime(javaExecutionTime, dbExecutionTime);
    }

    //3. Get all students from a given city having a given teacher
    //Retrieve the city, retrieve all faculties located into the given city, retrieve teacher, retrieve matching students
    public ExecutionTime executeQueryNo3(){
        long startTime = System.currentTimeMillis();
            Query cityCriteria = new Query().addCriteria(where("name").is("1QQKDOCX"));
            City city = mongoTemplate.findOne(cityCriteria, City.class);
            Query facultyCriteria = new Query().addCriteria(where("address.oid").is(city.oid));
            List<Faculty> faculties = mongoTemplate.find(facultyCriteria, Faculty.class);
            Query teacherCriteria = new Query().addCriteria(where("surname").is("3h4Iyg1S"));
            Teacher teacher = mongoTemplate.find(teacherCriteria, Teacher.class).stream().findFirst().get();
            List<Faculty> foundedFaculties = faculties.stream().filter(faculty -> faculty.teachers.contains(teacher.id)).collect(toList());
            Set<Integer> studentsIDs = foundedFaculties.stream().flatMap(f -> f.students.stream()).collect(toSet());
            List<Student> students = studentsIDs.stream().map(id -> mongoTemplate.findById(id, Student.class)).collect(toList());
        long endTime = System.currentTimeMillis();
        long javaExecutionTime = endTime - startTime;

        Integer cityTime = timeOfFilteredQuery("city", "name", "1QQKDOCX");
        Integer teacherTime = timeOfFilteredQuery("teacher", "surname", "3h4Iyg1S");
        Integer facultiesTime = timeOfFilteredQuery("faculty", "address.oid", city.oid);
        Integer studentsTime = studentsIDs.stream().mapToInt(id -> timeOfFilteredQuery("student", "_id", id)).sum();
        long dbExecutionTime = cityTime + teacherTime + facultiesTime + studentsTime;
        return new ExecutionTime(javaExecutionTime, dbExecutionTime);
    }

    //4. Find which department from a given faculty has the most credits
    // Find all departments from each faculty, for each department compute the total of credits.
    // Limit 1000. Unfortunately a large limit => ETA
    //Very un-performant
    public ExecutionTime executeQueryNo4() {
        long startTime = System.currentTimeMillis();
            List<Faculty> faculties = mongoTemplate.find(new Query().limit(1000), Faculty.class);
            Department department = faculties.stream()
                    .flatMap(faculty -> faculty.departments.stream())
                    .max(comparing(this::numberOfCredits)).get();
        long endTime = System.currentTimeMillis();
        long javaExecutionTime = endTime - startTime;

        Integer facultiesTime = timeOfLimitQuery("faculty", 1000);
        List<Integer> allSubjects = faculties.stream()
                .flatMap(faculty -> faculty.departments.stream()
                        .flatMap(dept -> dept.subjects.stream()))
                .collect(toList());
        Integer teachersTime = allSubjects.stream()
                .mapToInt(id -> timeOfFilteredQuery("subject", "_id", id))
                .sum();
        long dbExecutionTime = facultiesTime + teachersTime;
        return new ExecutionTime(javaExecutionTime, dbExecutionTime);
    }

    public Integer numberOfCredits(Department department){
        return department.subjects.stream()
                .mapToInt(subject -> mongoTemplate.findById(subject,Subject.class).credits)
                .sum();
    }
    //5. Find the university with the biggest number of students.
    // Find faculties limited to 100 and based on the faculty with the largest students retrieve the university.
    public ExecutionTime executeQueryNo5() {
        long startTime = System.currentTimeMillis();
            List<Faculty> faculties = mongoTemplate.find(new Query().limit(100), Faculty.class);
            Faculty facultyWithMostStudents = faculties.stream().max(comparing(faculty -> faculty.students.size())).orElseThrow(IllegalStateException::new);
            University university = mongoTemplate.find(new Query().addCriteria(where("uid").is(facultyWithMostStudents.uid)), University.class)
                    .stream().findFirst().get();
        long endTime = System.currentTimeMillis();
        long javaExecutionTime = endTime - startTime;

        Integer facultiesTime = timeOfLimitQuery("faculty", 100);
        Integer universityTime = timeOfFilteredQuery("university", "uid", facultyWithMostStudents.uid);
        long dbExecutionTime = facultiesTime + universityTime;
        return new ExecutionTime(javaExecutionTime, dbExecutionTime);
    }

    //6. Find the university with the biggest number of students at a course
    //Find subject, faculties which have a department containing the subject, final find university
    public ExecutionTime executeQueryNo6(){
        long startTime = System.currentTimeMillis();
            Query subjectName = new Query().addCriteria(where("name").is("JmazDxeq"));
            Subject subject = mongoTemplate.findOne(subjectName, Subject.class);
            Query query = new Query().addCriteria(where("departments.subjects").is(subject.id));
            List<Faculty> faculties = mongoTemplate.find(query, Faculty.class);
            Faculty facultyWithMostStudents = faculties.stream().max(comparing(f -> f.students.size())).orElseThrow(IllegalStateException::new);
        long endTime = System.currentTimeMillis();
        long javaExecutionTime = endTime - startTime;

        Integer subjectTime = timeOfFilteredQuery("subject", "name", "JmazDxeq");
        Integer facultiesTime = timeOfFilteredQuery("faculty", "departments.subjects", subject.id);
        long dbExecutionTime = subjectTime + facultiesTime;
        return new ExecutionTime(javaExecutionTime, dbExecutionTime);
    }


    //7. The oldest teacher from a country.
    public ExecutionTime executeQueryNo7(){
        long startTime = System.currentTimeMillis();
            Query countryName = new Query().addCriteria(where("name").is("YL17kq2"));
            Country country = mongoTemplate.findOne(countryName, Country.class);
            Query query = new Query().addCriteria(where("tid").is(country.id));
            List<City> cities = mongoTemplate.find(query, City.class);
            List<Integer> citiesIDs = cities.stream().map(city -> city.oid).collect(toList());
            Criteria matchAddress = where("address.oid").in(citiesIDs);
            List<Faculty> faculties = mongoTemplate.find(new Query().addCriteria(matchAddress), Faculty.class);
            Set<Integer> teachersIDs = faculties.stream().flatMap(faculty -> faculty.teachers.stream()).collect(toSet());
            Teacher oldestTeacher = teachersIDs.stream()
                    .map(id -> mongoTemplate.findById(id, Teacher.class))
                    .max(comparing(teacher -> teacher.age))
                    .orElseThrow(IllegalStateException::new);
        long endTime = System.currentTimeMillis();
        long javaExecutionTime = endTime - startTime;

        Integer countryTime = timeOfFilteredQuery("country", "name", "YL17kq2");
        Integer citiesTime = timeOfFilteredQuery("city", "tid", country.id);
        Integer teacherTime = teachersIDs.stream()
                .mapToInt(id -> timeOfFilteredQuery("teacher","_id",id))
                .sum();
        long dbExecutionTime = countryTime + citiesTime + teacherTime;
        return new ExecutionTime(javaExecutionTime, dbExecutionTime);
    }

    //8. The faculty with the highest student age mean
    //Retrieve all faculties, for each faculty compute the mean of students
    public ExecutionTime executeQueryNo8(){
        long startTime = System.currentTimeMillis();
            List<Faculty> faculties = mongoTemplate.find(new Query().limit(10), Faculty.class);
            Faculty faculty = faculties.stream().max(comparing(this::ageMeanOfStudents)).orElseThrow(IllegalStateException::new);
        long endTime = System.currentTimeMillis();
        long javaExecutionTime = endTime - startTime;

        Integer facultiesTime = timeOfLimitQuery("faculty", 100);
        int overallTime = 0;
        for (Faculty f : faculties) {
            int studentsTime = f.students.stream().mapToInt(id -> timeOfFilteredQuery("student", "_id", id)).sum();
            overallTime += studentsTime;
        }
        long dbExecutionTime = facultiesTime + overallTime;
        return new ExecutionTime(javaExecutionTime, dbExecutionTime);
    }

    private Double ageMeanOfStudents(Faculty faculty) {
        return faculty.students.stream().mapToDouble(id -> mongoTemplate.findById(id, Student.class).age).average().orElse(0.0d);
    }


    //9. The students from a given country, studying a course with the least number of credits of that teacher
    public ExecutionTime executeQueryNo9(){
        long startTime = System.currentTimeMillis();
            //Find all faculties from a given country
            Query countryCriteria = new Query().addCriteria(where("name").is("TlXzImx"));
            Country country = mongoTemplate.findOne(countryCriteria, Country.class);
            Query cityCriteria = new Query().addCriteria(where("tid").is(country.id));
            List<City> cities = mongoTemplate.find(cityCriteria, City.class);
            List<Integer> citiesIDs = cities.stream().map(city -> city.oid).collect(toList());
            Criteria matchAddress = where("address.oid").in(citiesIDs);
            List<Faculty> faculties = mongoTemplate.find(new Query().addCriteria(matchAddress), Faculty.class);

            //Find the given teacher
            Query teacherCriteria = new Query().addCriteria(where("surname").is("SvSpzjh6"));
            Teacher teacher = mongoTemplate.find(teacherCriteria, Teacher.class).stream().findFirst().get();

            //Find subject with least credits
            Integer subjectID = teacher.subjects.stream().min(comparing(id -> mongoTemplate.findById(id, Subject.class).credits)).get();

            //Match students from a given country which have the found subject in one of the departments
            List<Faculty> filteredFaculties = faculties.stream()
                    .filter(f -> f.departments.stream()
                            .anyMatch(d -> d.subjects.contains(subjectID)))
                    .collect(toList());
            Set<Integer> studentsIDS = filteredFaculties.stream().flatMap(f -> f.students.stream()).collect(toSet());
            List<Student> students = studentsIDS.stream().map(id -> mongoTemplate.findById(id, Student.class)).collect(toList());
        long endTime = System.currentTimeMillis();
        long javaExecutionTime = endTime - startTime;

        Integer countryTime = timeOfFilteredQuery("country", "name", "TlXzImx");
        Integer cityTime = timeOfFilteredQuery("city", "tid", country.id);
        Integer teacherTime = timeOfFilteredQuery("teacher", "address.oid", "SvSpzjh6");
        Integer subjectTime = teacher.subjects.stream().mapToInt(id -> timeOfFilteredQuery("subject", "_id", id)).sum();
        Integer studentsTime = studentsIDS.stream().mapToInt(id -> timeOfFilteredQuery("student", "_id", id)).sum();
        long dbExecutionTime = countryTime + cityTime + teacherTime + subjectTime + studentsTime;
        return new ExecutionTime(javaExecutionTime, dbExecutionTime);
    }



    //10. The faculty with the least number of departments, but with the most students
    //Very un-performant when fetching 300.000 records. Limit added to 1000.
    //DB duration time is 0 since there in just one database access and the limit of records needed is low.
    public ExecutionTime executeQueryNo10(){
        long startTime = System.currentTimeMillis();
            List<Faculty> faculties = mongoTemplate.find(new Query().limit(1000), Faculty.class);
            List<Faculty> sortedFaculties = faculties.stream()
                    .sorted(comparing((Faculty faculty) -> faculty.departments.size()))
                    .collect(toList());
            Faculty firstFaculty = sortedFaculties.get(0);
            int deptMaxSize = firstFaculty.departments.size();
            int studsLen = firstFaculty.students.size();
            int pos = 0;
            for (int i = 1; i < sortedFaculties.size(); i++) {
                Faculty faculty = sortedFaculties.get(i);
                if (faculty.departments.size() != deptMaxSize)
                    break;
                if (studsLen < faculty.students.size()) {
                    studsLen = faculty.students.size();
                    pos = i;
                }
            }
            Faculty theOne = sortedFaculties.get(pos);

        long endTime = System.currentTimeMillis();
        long javaExecutionTime = endTime - startTime;

        long dbExecutionTime = timeOfLimitQuery("faculty", 1000);
        return new ExecutionTime(javaExecutionTime, dbExecutionTime);
    }

    private Integer timeOfFilteredQuery(String collectionName, String columnName, Object columnValue) {
        BasicDBObject findQuery = new BasicDBObject("find", collectionName);
        BasicDBObject query = new BasicDBObject(columnName, columnValue);
        findQuery.append("filter", query);
        BasicDBObject explainQuery = new BasicDBObject("explain", findQuery);
        Document document = mongoDB.runCommand(explainQuery);
        Document executionStats = (Document) document.get("executionStats");
        return executionStats.getInteger("executionTimeMillis");
    }

    private Integer timeOfLimitQuery(String collectionName, Integer limit) {
        BasicDBObject findQuery = new BasicDBObject("find", collectionName);
        findQuery.append("limit", limit);
        BasicDBObject explainQuery = new BasicDBObject("explain", findQuery);
        Document document = mongoDB.runCommand(explainQuery);
        Document executionStats = (Document) document.get("executionStats");
        return executionStats.getInteger("executionTimeMillis");
    }


}
