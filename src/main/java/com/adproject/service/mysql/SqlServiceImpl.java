package com.adproject.service.mysql;

import com.adproject.db.datasource.TemplateFactory;
import com.adproject.db.datasource.template.MySQLTemplate;
import com.adproject.utils.ExecutionTime;
import org.springframework.stereotype.Service;

@Service
public class SqlServiceImpl implements com.adproject.service.Service {

    //1. Get all students together with address, city, ...
    public ExecutionTime executeQueryNo1() {
        String query = "select * from student s, address a, city ct, country c " +
                        "where s.address_id = a.id and a.city_id = ct.id and ct.country_id = c.id";
        return timeOf(query);
    }

    //2. Get all students from a given city having a given teacher
    public ExecutionTime executeQueryNo2() {
        String query = "select * from student s, faculty_student fs, faculty f, faculty_teacher ft, teacher t where " +
                       "s.forename = '2Ue' and s.id = fs.student_id and fs.faculty_id = f.id and f.id = ft.faculty_id and ft.teacher_id = t.id";
        return timeOf(query);
    }

    //3.Get all students from a given city having a given teacher
    public ExecutionTime executeQueryNo3() {
        String query = "select s.id, s.surname, s.age from student s, address a, city c, faculty_student fs, faculty f, faculty_teacher ft, teacher t " +
                "where c.name = 'GcC' and c.id = a.city_id and a.id = s.address_id " +
                "and s.id = fs.student_id and fs.faculty_id = f.id " +
                "and f.id = ft.faculty_id and ft.teacher_id = t.id and t.forename = '3nM' ";
        return timeOf(query);
    }

    //4. Find which department from a given faculty has the most credits
    public ExecutionTime executeQueryNo4() {
        String query = "select max(the_sum), atb.the_id from (select d.id the_id, sum(s.credits) the_sum from department d, subject_department sd, subject s " +
                "where d.id = sd.department_id and sd.subject_id = s.id group by d.id) as atb";
        return timeOf(query);
    }

    //5. Find the university with the biggest number of students
    public ExecutionTime executeQueryNo5() {
        String query = "select max(numero), dummy.uname from (select count(s.id) numero, u.name uname from faculty f, faculty_student fs, student s, university u " +
                "where f.university_id = u.id and f.id = fs.faculty_id and fs.student_id = s.id group by u.id) dummy";
        return timeOf(query);
    }

    //6. Find the university with the biggest number of students at a course
    public ExecutionTime executeQueryNo6() {
        String query = "select max(numero), dummy.uname from (select count(s.id) numero, u.name uname from faculty f, faculty_student fs, student s, university u, department d, subject_department sd, subject ss " +
                "where f.university_id = u.id and f.id = fs.faculty_id and fs.student_id = s.id and d.faculty_id = f.id and sd.department_id = d.id and sd.subject_id = ss.id and ss.name = '00M' group by u.id) dummy";
        return timeOf(query);
    }

    //7. The oldest teacher from a country
    public ExecutionTime executeQueryNo7() {
        String query = "select t.id oldest, max(t.age) age from country c, city ct, address a, faculty f, faculty_teacher ft, teacher t " +
                "where c.name ='G33' and c.id = ct.country_id and ct.id = a.city_id and a.id = f.address_id " +
                "and f.id = ft.faculty_id and ft.teacher_id = t.id group by t.id";
        return timeOf(query);
    }

    //8. The faculty with the highest student age mean
    public ExecutionTime executeQueryNo8() {
        String query = "select dummy.facultyid, max(avv) from (select f.id facultyid, avg(s.age) avv from faculty f, faculty_student fs, student s " +
                "where f.id = fs.faculty_id and fs.student_id = s.id group by f.id) dummy";
        return timeOf(query);
    }
    //9. The students from a given country, studying a course with the least number of credits of that teacher
    public ExecutionTime executeQueryNo9() {
        String query = "select s.id from address a, city c, country cc, faculty f, faculty_student fs, student s, university u, department d, subject_department sd, subject ss, " +
                "(select ss.id smallest, min(ss.credits) from teacher t, subject_teacher st, subject ss where  t.id = st.teacher_id and st.subject_id = ss.id and t.id = 3 group by t.id) dummy " +
                "where s.address_id = a.id and a.city_id = c.id and c.country_id = cc.id " +
                "and cc.id = 'some' and f.university_id = u.id " +
                "and f.id = fs.faculty_id and fs.student_id = s.id and d.faculty_id = f.id and sd.department_id = d.id " +
                "and sd.subject_id = ss.id and ss.name = dummy.smallest";
        return new ExecutionTime(150L, 75L);
    }

    //10. The students from a given country, studying a course with the least number of credits of that teacher
    public ExecutionTime executeQueryNo10() {
        String query = "select * from country c, city ct, address a, faculty f, faculty_teacher ft, teacher t, subject_teacher st, subject sss , " +
                "(select subject.id lala, min(subject.credits) from country c, city ct, address a, faculty f, faculty_teacher ft, teacher t, subject_teacher st, subject " +
                "where t.id= 5 and c.id = ct.country_id and ct.id = a.city_id and a.id = f.address_id " +
                "and f.id = ft.faculty_id and ft.teacher_id = t.id and st.teacher_id = t.id and st.subject_id = subject.id) dummy " +
                "where  c.id = 1 and c.id = ct.country_id and ct.id = a.city_id and a.id = f.address_id " +
                "and f.id = ft.faculty_id and ft.teacher_id = t.id and st.teacher_id = t.id and st.subject_id = sss.id and sss.id = dummy.lala";
        return timeOf(query);
    }

    private ExecutionTime timeOf(String query){
        long start = System.currentTimeMillis();
        MySQLTemplate template = TemplateFactory.getTemplate(MySQLTemplate.class);
        String analyzeQuery = "explain analyze " + query;
        final String result = template.queryForObject(analyzeQuery, String.class);
        long end = System.currentTimeMillis();
        long dbExecutionTime = (long) dbTimeFromString(result);
        return new ExecutionTime(end - start,dbExecutionTime);
    }

    private double dbTimeFromString(String result) {
        String actual = "actual time=";
        int startPos = result.indexOf(actual) + actual.length();
        int endPos = result.indexOf("..");
        String parsed = result.substring(startPos, endPos);
        double time = Double.parseDouble(parsed);
        return (time > 1d) ? time * 10: time * 1000;
    }
}
