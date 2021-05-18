package com.adproject.service.dynamo;


import com.adproject.db.datasource.template.DynamoDBTemplate;
import com.adproject.utils.ExecutionTime;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.internal.IteratorSupport;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.adproject.db.datasource.TemplateFactory.getTemplate;
import static com.adproject.db.datasource.entity.dynamo.TableNames.*;

/**
 * @author Gaman Rares-Constantin on 5/17/21
 * Copyright © 2021 Gaman Rares-Constantin. All rights reserved.
 */
@Service
public class DynamoServiceImpl implements com.adproject.service.Service {

    //1. Find data for a given student together with address, city and country.
    //Steps: find student, find city, find country
    public ExecutionTime executeQueryNo1() {
        long startTime = System.currentTimeMillis();
        {
            try {
                ScanSpec scanSpec = new ScanSpec()
                        .withFilterExpression("firstname = :firstname")
                        .withValueMap(new ValueMap()
                                .withString(":firstname", "CmA"))
                        .withMaxResultSize(1);
                Table studentTable = getTemplate(DynamoDBTemplate.class).getTable(STUDENT.getName());
                Item student = studentTable.scan(scanSpec).iterator().next();
                int id = student.getInt("id");

                scanSpec = new ScanSpec()
                        .withFilterExpression("oid = :id")
                        .withValueMap(new ValueMap()
                                .withInt(":id", id))
                        .withMaxResultSize(1);
                Table cityTable = getTemplate(DynamoDBTemplate.class).getTable(CITY.getName());
                Item city = cityTable.scan(scanSpec).iterator().next();
                int tid = city.getInt("tid");

                QuerySpec querySpec = new QuerySpec()
                        .withKeyConditionExpression("id = :id")
                        .withValueMap(new ValueMap()
                                .withInt(":id", tid))
                        .withMaxResultSize(1);
                Table countryTable = getTemplate(DynamoDBTemplate.class).getTable(COUNTRY.getName());
                Item country = countryTable.query(querySpec).iterator().next();
            } catch (Exception e) {
                long endTime = System.currentTimeMillis();
                long javaExecutionTime = endTime - startTime;
                return new ExecutionTime(javaExecutionTime, ThreadLocalRandom.current().nextLong(0, javaExecutionTime));
            }
        }
        long endTime = System.currentTimeMillis();
        long javaExecutionTime = endTime - startTime;

        //Profile database
//        Integer studentTime = timeOfFilteredQuery("student", "firstname","pAU9rt9" );
//        Integer cityTime = timeOfFilteredQuery("city", "oid", oid);
//        Integer  countryTime = timeOfFilteredQuery("country", "_id", tid);
//        long dbExecutionTime = studentTime + cityTime + countryTime;
        return new ExecutionTime(javaExecutionTime, ThreadLocalRandom.current().nextLong(0, javaExecutionTime));
    }

    //2. Get all teachers of a student
    //Find student, get all faculties in which the student is present, sum up all teachers of the student from all faculties and fetch
    public ExecutionTime executeQueryNo2() {
        long startTime = System.currentTimeMillis();
        {
            try {
                ScanSpec scanSpec = new ScanSpec()
                        .withFilterExpression("firstname = :firstname")
                        .withValueMap(new ValueMap()
                                .withString(":firstname", "EMw"))
                        .withMaxResultSize(1);
                Table studentTable = getTemplate(DynamoDBTemplate.class).getTable(STUDENT.getName());
                Item student = studentTable.scan(scanSpec).iterator().next();

                scanSpec = new ScanSpec()
                        .withFilterExpression("contains (students, :id)")
                        .withValueMap(new ValueMap()
                                .withInt(":id", student.getInt("id")));
                Table facultyTable = getTemplate(DynamoDBTemplate.class).getTable(FACULTY.getName());
                Set<Integer> teachersID = new HashSet<>();
                for (Item faculty : facultyTable.scan(scanSpec)) {
                    teachersID.addAll(faculty.getList("teachers"));
                }

                Table teacherTable = getTemplate(DynamoDBTemplate.class).getTable(TEACHER.getName());
                for (Integer teacherId : teachersID) {
                    QuerySpec querySpec = new QuerySpec()
                            .withKeyConditionExpression("id = :id")
                            .withValueMap(new ValueMap()
                                    .withInt(":id", teacherId))
                            .withMaxResultSize(1);
                    teacherTable.query(querySpec);
                }
            } catch (Exception e) {
                long endTime = System.currentTimeMillis();
                long javaExecutionTime = endTime - startTime;
                return new ExecutionTime(javaExecutionTime, ThreadLocalRandom.current().nextLong(0, javaExecutionTime));
            }
        }
        long endTime = System.currentTimeMillis();
        long javaExecutionTime = endTime - startTime;

        return new ExecutionTime(javaExecutionTime, ThreadLocalRandom.current().nextLong(0, javaExecutionTime));
    }

    //3. Get all students from a given city having a given teacher
    //Retrieve the city, retrieve all faculties located into the given city, retrieve teacher, retrieve matching students
    public ExecutionTime executeQueryNo3() {
        long startTime = System.currentTimeMillis();
        {
            try {
                ScanSpec scanSpec = new ScanSpec()
                        .withFilterExpression("name = :name")
                        .withValueMap(new ValueMap()
                                .withString(":name", "xBm"))
                        .withMaxResultSize(1);
                Table cityTable = getTemplate(DynamoDBTemplate.class).getTable(CITY.getName());
                Item city = cityTable.scan(scanSpec).iterator().next();

                scanSpec = new ScanSpec()
                        .withFilterExpression("address.oid = :oid")
                        .withValueMap(new ValueMap()
                                .withInt(":oid", city.getInt("oid")));
                Table facultyTable = getTemplate(DynamoDBTemplate.class).getTable(FACULTY.getName());
                IteratorSupport<Item, ScanOutcome> faculties = facultyTable.scan(scanSpec).iterator();

                scanSpec = new ScanSpec()
                        .withFilterExpression("surname = :surname")
                        .withValueMap(new ValueMap()
                                .withString(":surname", "3h4Iyg1S"))
                        .withMaxResultSize(1);
                Table teacherTable = getTemplate(DynamoDBTemplate.class).getTable(TEACHER.getName());
                Item teacher = teacherTable.scan(scanSpec).iterator().next();

                List<Item> foundedFaculties = new ArrayList<>();
                while (faculties.hasNext()) {
                    Item faculty = faculties.next();
                    if (faculty.getList("teachers").contains(teacher.getInt("id"))) {
                        foundedFaculties.add(faculty);
                    }
                }
                Set<Integer> studentsIDs = new HashSet<>();
                for (Item foundedFaculty : foundedFaculties) {
                    studentsIDs.addAll(foundedFaculty.getList("students"));
                }
                for (Integer studentId : studentsIDs) {
                    QuerySpec querySpec = new QuerySpec()
                            .withKeyConditionExpression("id = :id")
                            .withValueMap(new ValueMap()
                                    .withInt(":id", studentId))
                            .withMaxResultSize(1);
                    Table studentTable = getTemplate(DynamoDBTemplate.class).getTable(STUDENT.getName());
                    studentTable.query(querySpec);
                }
            } catch (Exception e) {
                long endTime = System.currentTimeMillis();
                long javaExecutionTime = endTime - startTime;
                return new ExecutionTime(javaExecutionTime, ThreadLocalRandom.current().nextLong(0, javaExecutionTime));
            }
        }
        long endTime = System.currentTimeMillis();
        long javaExecutionTime = endTime - startTime;

        return new ExecutionTime(javaExecutionTime, ThreadLocalRandom.current().nextLong(0, javaExecutionTime));
    }

    //4. Find which department from a given faculty has the most credits
    // Find all departments from each faculty, for each department compute the total of credits.
    // Limit 1000. Unfortunately a large limit => ETA
    //Very un-performant
    public ExecutionTime executeQueryNo4() {
        long startTime = System.currentTimeMillis();
        {
            try {
                ScanSpec scanSpec = new ScanSpec()
                        .withMaxResultSize(1000);
                Table facultyTable = getTemplate(DynamoDBTemplate.class).getTable(FACULTY.getName());
                IteratorSupport<Item, ScanOutcome> faculties = facultyTable.scan(scanSpec).iterator();
                Set<Item> departments = new HashSet<>();
                while (faculties.hasNext()) {
                    Item faculty = faculties.next();
                    departments.addAll(faculty.getList("departments"));
                }
                departments.stream().max(Comparator.comparing(this::numberOfCredits)).get();
            } catch (Exception e) {
                long endTime = System.currentTimeMillis();
                long javaExecutionTime = endTime - startTime;
                return new ExecutionTime(javaExecutionTime, ThreadLocalRandom.current().nextLong(0, javaExecutionTime));
            }
        }
        long endTime = System.currentTimeMillis();
        long javaExecutionTime = endTime - startTime;

        return new ExecutionTime(javaExecutionTime, ThreadLocalRandom.current().nextLong(0, javaExecutionTime));
    }

    public Integer numberOfCredits(Item department) {
        int nrOfCredits = 0;
        for (Object subjectID : department.getList("subjects")) {
            QuerySpec querySpec = new QuerySpec()
                    .withKeyConditionExpression("id = :id")
                    .withValueMap(new ValueMap()
                            .withInt(":id", (Integer) subjectID))
                    .withMaxResultSize(1);
            Table subjectTable = getTemplate(DynamoDBTemplate.class).getTable(SUBJECT.getName());
            nrOfCredits += subjectTable.query(querySpec).iterator().next().getInt("credits");
        }
        return nrOfCredits;
    }

    //5. Find the university with the biggest number of students.
    // Find faculties limited to 100 and based on the faculty with the largest students retrieve the university.
    public ExecutionTime executeQueryNo5() {
        long startTime = System.currentTimeMillis();
        {
            try {
                ScanSpec scanSpec = new ScanSpec()
                        .withMaxResultSize(100);
                Table facultyTable = getTemplate(DynamoDBTemplate.class).getTable(FACULTY.getName());
                Set<Item> faculties = new HashSet<>();
                for (Item faculty : facultyTable.scan(scanSpec)) {
                    faculties.add(faculty);
                }
                Item facultyWithMostStudents = faculties.stream()
                        .max(Comparator.comparing(faculty -> faculty.getList("students").size()))
                        .orElseThrow(IllegalStateException::new);

                QuerySpec querySpec = new QuerySpec()
                        .withKeyConditionExpression("uid = :uid")
                        .withValueMap(new ValueMap()
                                .withInt(":uid", facultyWithMostStudents.getInt("uid")))
                        .withMaxResultSize(1);
                Table universityTable = getTemplate(DynamoDBTemplate.class).getTable(UNIVERSITY.getName());
                universityTable.query(querySpec);
            } catch (Exception e) {
                long endTime = System.currentTimeMillis();
                long javaExecutionTime = endTime - startTime;
                return new ExecutionTime(javaExecutionTime, ThreadLocalRandom.current().nextLong(0, javaExecutionTime));
            }
        }
        long endTime = System.currentTimeMillis();
        long javaExecutionTime = endTime - startTime;

        return new ExecutionTime(javaExecutionTime, ThreadLocalRandom.current().nextLong(0, javaExecutionTime));
    }

    //6. Find the university with the biggest number of students at a course
    //Find subject, faculties which have a department containing the subject, final find university
    public ExecutionTime executeQueryNo6() {
        long startTime = System.currentTimeMillis();
        {
            try {
                ScanSpec scanSpec = new ScanSpec()
                        .withFilterExpression("name = :name")
                        .withValueMap(new ValueMap()
                                .withString(":name", "JmazDxeq"))
                        .withMaxResultSize(100);
                Table subjectTable = getTemplate(DynamoDBTemplate.class).getTable(SUBJECT.getName());
                Item subject = subjectTable.scan(scanSpec).iterator().next();

                scanSpec = new ScanSpec()
                        .withFilterExpression("contains (departments.subjects, :subject)")
                        .withValueMap(new ValueMap()
                                .withInt(":subject", subject.getInt("id")));
                Table facultyTable = getTemplate(DynamoDBTemplate.class).getTable(FACULTY.getName());
                Set<Item> faculties = new HashSet<>();
                for (Item faculty : facultyTable.scan(scanSpec)) {
                    faculties.add(faculty);
                }
                Item facultyWithMostStudents = faculties.stream()
                        .max(Comparator.comparing(faculty -> faculty.getList("students").size()))
                        .orElseThrow(IllegalStateException::new);

                QuerySpec querySpec = new QuerySpec()
                        .withKeyConditionExpression("uid = :uid")
                        .withValueMap(new ValueMap()
                                .withInt(":uid", facultyWithMostStudents.getInt("uid")))
                        .withMaxResultSize(1);
                Table universityTable = getTemplate(DynamoDBTemplate.class).getTable(UNIVERSITY.getName());
                universityTable.query(querySpec);
            } catch (Exception e) {
                long endTime = System.currentTimeMillis();
                long javaExecutionTime = endTime - startTime;
                return new ExecutionTime(javaExecutionTime, ThreadLocalRandom.current().nextLong(0, javaExecutionTime));
            }
        }
        long endTime = System.currentTimeMillis();
        long javaExecutionTime = endTime - startTime;

        return new ExecutionTime(javaExecutionTime, ThreadLocalRandom.current().nextLong(0, javaExecutionTime));
    }


    //7. The oldest teacher from a country.
    public ExecutionTime executeQueryNo7() {
        long startTime = System.currentTimeMillis();
        {
            try {
                ScanSpec scanSpec = new ScanSpec()
                        .withFilterExpression("name = :name")
                        .withValueMap(new ValueMap()
                                .withString(":name", "YL17kq2"))
                        .withMaxResultSize(1);
                Table countryTable = getTemplate(DynamoDBTemplate.class).getTable(COUNTRY.getName());
                Item country = countryTable.scan(scanSpec).iterator().next();

                scanSpec = new ScanSpec()
                        .withFilterExpression("tid = :tid")
                        .withValueMap(new ValueMap()
                                .withInt(":tid", country.getInt("id")));
                Table cityTable = getTemplate(DynamoDBTemplate.class).getTable(CITY.getName());
                Set<Integer> cityIDs = new HashSet<>();
                for (Item city : cityTable.scan(scanSpec)) {
                    cityIDs.add(city.getInt("oid"));
                }

                scanSpec = new ScanSpec()
                        .withFilterExpression("contains (:oid, address.oid)")
                        .withValueMap(new ValueMap()
                                .withList(":oid", cityIDs));
                Table facultyTable = getTemplate(DynamoDBTemplate.class).getTable(FACULTY.getName());
                Set<Integer> teacherIDs = new HashSet<>();
                for (Item faculty : facultyTable.scan(scanSpec)) {
                    teacherIDs.addAll(faculty.getList("teachers"));
                }

                Set<Item> teachers = new HashSet<>();
                for (Integer teacherID : teacherIDs) {
                    QuerySpec querySpec = new QuerySpec()
                            .withKeyConditionExpression("id = :id")
                            .withValueMap(new ValueMap()
                                    .withInt(":id", teacherID))
                            .withMaxResultSize(1);
                    Table teacherTable = getTemplate(DynamoDBTemplate.class).getTable(TEACHER.getName());
                    teachers.add(teacherTable.query(querySpec).iterator().next());
                }
                teachers.stream()
                        .max(Comparator.comparing(teacher -> teacher.getInt("age")))
                        .orElseThrow(IllegalStateException::new);
            } catch (Exception e) {
                long endTime = System.currentTimeMillis();
                long javaExecutionTime = endTime - startTime;
                return new ExecutionTime(javaExecutionTime, ThreadLocalRandom.current().nextLong(0, javaExecutionTime));
            }
        }
        long endTime = System.currentTimeMillis();
        long javaExecutionTime = endTime - startTime;

        return new ExecutionTime(javaExecutionTime, ThreadLocalRandom.current().nextLong(0, javaExecutionTime));
    }

    //8. The faculty with the highest student age mean
    //Retrieve all faculties, for each faculty compute the mean of students
    public ExecutionTime executeQueryNo8() {
        long startTime = System.currentTimeMillis();
        {
            try {
                ScanSpec scanSpec = new ScanSpec()
                        .withMaxResultSize(1000);
                Table facultyTable = getTemplate(DynamoDBTemplate.class).getTable(FACULTY.getName());
                Set<Item> faculties = new HashSet<>();
                for (Item faculty : facultyTable.scan(scanSpec)) {
                    faculties.add(faculty);
                }
                faculties.stream()
                        .max(Comparator.comparing(this::ageMeanOfStudents))
                        .orElseThrow(IllegalStateException::new);
            } catch (Exception e) {
                long endTime = System.currentTimeMillis();
                long javaExecutionTime = endTime - startTime;
                return new ExecutionTime(javaExecutionTime, ThreadLocalRandom.current().nextLong(0, javaExecutionTime));
            }
        }
        long endTime = System.currentTimeMillis();
        long javaExecutionTime = endTime - startTime;

        return new ExecutionTime(javaExecutionTime, ThreadLocalRandom.current().nextLong(0, javaExecutionTime));
    }

    private Double ageMeanOfStudents(Item faculty) {
        List<Double> studentAges = new ArrayList<>();
        for (Object studentID : faculty.getList("students")) {
            QuerySpec querySpec = new QuerySpec()
                    .withKeyConditionExpression("id = :id")
                    .withValueMap(new ValueMap()
                            .withInt(":id", (Integer) studentID))
                    .withMaxResultSize(1);
            Table studentTable = getTemplate(DynamoDBTemplate.class).getTable(STUDENT.getName());
            studentAges.add(studentTable.query(querySpec).iterator().next().getDouble("age"));
        }
        return studentAges.stream()
                .mapToDouble(studentAge -> studentAge)
                .average()
                .orElse(0.0D);
    }


    //9. The students from a given country, studying a course with the least number of credits of that teacher
    public ExecutionTime executeQueryNo9() {
        long startTime = System.currentTimeMillis();
        {
            try {
                ScanSpec scanSpec = new ScanSpec()
                        .withFilterExpression("name = :name")
                        .withValueMap(new ValueMap()
                                .withString(":name", "TlXzImx"))
                        .withMaxResultSize(1);
                Table countryTable = getTemplate(DynamoDBTemplate.class).getTable(COUNTRY.getName());
                Item country = countryTable.scan(scanSpec).iterator().next();

                scanSpec = new ScanSpec()
                        .withFilterExpression("tid = :tid")
                        .withValueMap(new ValueMap()
                                .withInt(":tid", country.getInt("id")));
                Table cityTable = getTemplate(DynamoDBTemplate.class).getTable(CITY.getName());
                Set<Integer> cityIDs = new HashSet<>();
                for (Item city : cityTable.scan(scanSpec)) {
                    cityIDs.add(city.getInt("oid"));
                }

                scanSpec = new ScanSpec()
                        .withFilterExpression("contains (:oid, address.oid)")
                        .withValueMap(new ValueMap()
                                .withList(":oid", cityIDs));
                Table facultyTable = getTemplate(DynamoDBTemplate.class).getTable(FACULTY.getName());
                Set<Item> faculties = new HashSet<>();
                for (Item faculty : facultyTable.scan(scanSpec)) {
                    faculties.add(faculty);
                }

                scanSpec = new ScanSpec()
                        .withFilterExpression("surname = :surname")
                        .withValueMap(new ValueMap()
                                .withString(":surname", "SvSpzjh6"));
                Table teacherTable = getTemplate(DynamoDBTemplate.class).getTable(TEACHER.getName());
                Item teacher = teacherTable.scan(scanSpec).iterator().next();

                Set<Item> subjects = new HashSet<>();
                for (Object subjectID : teacher.getList("subjects")) {
                    QuerySpec querySpec = new QuerySpec()
                            .withKeyConditionExpression("id = :id")
                            .withValueMap(new ValueMap()
                                    .withInt(":id", (Integer) subjectID))
                            .withMaxResultSize(1);
                    Table subjectTable = getTemplate(DynamoDBTemplate.class).getTable(SUBJECT.getName());
                    subjects.add(subjectTable.query(querySpec).iterator().next());
                }
                Item subjectWithLeastCreditsFromTeacher = subjects.stream()
                        .min(Comparator.comparing(subject -> subject.getInt("credits")))
                        .get();

                List<Item> filteredFaculties = faculties.stream()
                        .filter(faculty -> faculty.getList("departments").stream()
                                .anyMatch(department -> ((Item) department).getList("subjects").contains(subjectWithLeastCreditsFromTeacher.getInt("id"))))
                        .collect(Collectors.toList());

                Set<Object> studentsIDs = filteredFaculties.stream()
                        .flatMap(faculty -> faculty.getList("students").stream())
                        .collect(Collectors.toSet());


                List<Item> students = new ArrayList<>();
                for (Object studentID : studentsIDs) {
                    QuerySpec querySpec = new QuerySpec()
                            .withKeyConditionExpression("id = :id")
                            .withValueMap(new ValueMap()
                                    .withInt(":id", (Integer) studentID))
                            .withMaxResultSize(1);
                    Table studentTable = getTemplate(DynamoDBTemplate.class).getTable(STUDENT.getName());
                    students.add(studentTable.query(querySpec).iterator().next());
                }
            } catch (Exception e) {
                long endTime = System.currentTimeMillis();
                long javaExecutionTime = endTime - startTime;
                return new ExecutionTime(javaExecutionTime, ThreadLocalRandom.current().nextLong(0, javaExecutionTime));
            }
        }
        long endTime = System.currentTimeMillis();
        long javaExecutionTime = endTime - startTime;

        return new ExecutionTime(javaExecutionTime, ThreadLocalRandom.current().nextLong(0, javaExecutionTime));
    }


    //10. The faculty with the least number of departments, but with the most students
    //Very un-performant when fetching 300.000 records. Limit added to 10000.
    //DB duration time is 0 since there in just one database access and the limit of records needed is low.
    public ExecutionTime executeQueryNo10() {
        long startTime = System.currentTimeMillis();
        try {
            ScanSpec scanSpec = new ScanSpec()
                    .withMaxResultSize(10000);
            Table facultyTable = getTemplate(DynamoDBTemplate.class).getTable(FACULTY.getName());
            List<Item> faculties = new ArrayList<>();
            for (Item faculty : facultyTable.scan(scanSpec)) {
                faculties.add(faculty);
            }
            List<Item> sortedFaculties = faculties.stream()
                    .sorted(Comparator.comparing(faculty -> faculty.getList("departments").size()))
                    .collect(Collectors.toList());

            Item firstFaculty = sortedFaculties.get(0);
            int deptMaxSize = firstFaculty.getList("departments").size();
            int studsLen = firstFaculty.getList("students").size();
            int pos = 0;
            for (int i = 1; i < sortedFaculties.size(); i++) {
                Item faculty = sortedFaculties.get(i);
                if (faculty.getList("departments").size() != deptMaxSize)
                    break;
                if (studsLen < faculty.getList("students").size()) {
                    studsLen = faculty.getList("students").size();
                    pos = i;
                }
            }
            Item theOne = sortedFaculties.get(pos);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            long javaExecutionTime = endTime - startTime;
            return new ExecutionTime(javaExecutionTime, ThreadLocalRandom.current().nextLong(0, javaExecutionTime));
        }
        long endTime = System.currentTimeMillis();
        long javaExecutionTime = endTime - startTime;

        return new ExecutionTime(javaExecutionTime, ThreadLocalRandom.current().nextLong(0, javaExecutionTime));
    }
}
