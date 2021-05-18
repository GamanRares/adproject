package com.adproject;

import com.adproject.db.datasource.TemplateFactory;
import com.adproject.db.datasource.entity.mongo.Student;
import com.adproject.db.datasource.template.MySQLTemplate;
import com.adproject.service.mongo.MongoServiceImpl;
import com.adproject.service.mongo.SqlServiceImpl;
import com.adproject.utils.ExecutionTime;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static java.util.concurrent.ThreadLocalRandom.current;

@SpringBootTest
@RunWith(SpringRunner.class)
class AdprojectApplicationTests  {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    MongoServiceImpl mongoService;

    @Autowired
    SqlServiceImpl sqlService;

    @Test
    void contextLoads() {
        final String sqlRowSet = TemplateFactory.getTemplate(MySQLTemplate.class).queryForObject("SELECT s.surname FROM Student s WHERE s.id = 14", String.class);
        System.out.println(sqlRowSet);
    }

    @Test
    void testMongoDBConnection(){
        List<Student> faculties = mongoTemplate.findAll(Student.class);
        System.out.println(faculties.size());
    }

    @Test
    void testMongoQueryNo1(){
        ExecutionTime executionTime = mongoService.executeQueryNo1();
        System.out.println(executionTime);
    }

    @Test
    void testSQLQueryNo1(){
        ExecutionTime executionTime = sqlService.executeQueryNo1();
        System.out.println(executionTime);
    }

    @Test
    void testMongoQueryNo2(){
        ExecutionTime executionTime = mongoService.executeQueryNo2();
        System.out.println(executionTime);
    }

    @Test
    void testSQLQueryNo2(){
        ExecutionTime executionTime = sqlService.executeQueryNo2();
        System.out.println(executionTime);
    }

    @Test
    void testMongoQueryNo3(){
        ExecutionTime executionTime = mongoService.executeQueryNo3();
        System.out.println(executionTime);
    }

    @Test
    void testSQLQueryNo3(){
        ExecutionTime executionTime = sqlService.executeQueryNo3();
        System.out.println(executionTime);
    }

    @Test
    void testMongoQueryNo4(){
        ExecutionTime executionTime = mongoService.executeQueryNo4();
        System.out.println(executionTime);
    }

    @Test
    void testSQLQueryNo4(){
        ExecutionTime executionTime = sqlService.executeQueryNo4();
        System.out.println(executionTime);
    }

    @Test
    void testMongoQueryNo5(){
        ExecutionTime executionTime = mongoService.executeQueryNo5();
        System.out.println(executionTime);
    }

    @Test
    void testSQLQueryNo5(){
        ExecutionTime executionTime = sqlService.executeQueryNo5();
        System.out.println(executionTime);
    }

    @Test
    void testMongoQueryNo6(){
        ExecutionTime executionTime = mongoService.executeQueryNo6();
        System.out.println(executionTime);
    }

    @Test
    void testSQLQueryNo6(){
        ExecutionTime executionTime = sqlService.executeQueryNo6();
        System.out.println(executionTime);
    }

    @Test
    void testMongoQueryNo7(){
        ExecutionTime executionTime = mongoService.executeQueryNo7();
        System.out.println(executionTime);
    }

    @Test
    void testSQLQueryNo7(){
        ExecutionTime executionTime = sqlService.executeQueryNo7();
        System.out.println(executionTime);
    }

    @Test
    void testMongoQueryNo8(){
        ExecutionTime executionTime = mongoService.executeQueryNo8();
        System.out.println(executionTime);
    }

    @Test
    void testSQLQueryNo8(){
        ExecutionTime executionTime = sqlService.executeQueryNo8();
        System.out.println(executionTime);
    }

    @Test
    void testMongoQueryNo9(){
        ExecutionTime executionTime = mongoService.executeQueryNo9();
        System.out.println(executionTime);
    }

    @Test
    void testSQLQueryNo9(){
        ExecutionTime executionTime = sqlService.executeQueryNo9();
        System.out.println(executionTime);
    }

    @Test
    void testMongoQueryNo10(){
        ExecutionTime executionTime = mongoService.executeQueryNo10();
        System.out.println(executionTime);
    }

    @Test
    void testSQLQueryNo10(){
        ExecutionTime executionTime = sqlService.executeQueryNo10();
        System.out.println(executionTime);
    }

    //TODO Get all students together with address, city, ...

    //TODO Get all teachers of a student

    //TODO Get all students from a given city having a given teacher

    //TODO Find which department from a given faculty has the most credits

    //TODO Find the university with the biggest number of students

    //TODO Find the university with the biggest number of students at a course

    //TODO The oldest teacher form a country

    //TODO The faculty with the highest student age mean

    //TODO The students from a given country, studying a course with the least number of credits of that teacher

    //TODO The faculty with the least number of departments, but with the most students

    //between(LocalDate.of(1989, Month.OCTOBER, 14), LocalDate.now()
    public static LocalDate between(LocalDate startInclusive, LocalDate endExclusive) {
        long startEpochDay = startInclusive.toEpochDay();
        long endEpochDay = endExclusive.toEpochDay();
        long randomDay = current()
                .nextLong(startEpochDay, endEpochDay);

        return LocalDate.ofEpochDay(randomDay);
    }

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static Random rnd = new Random();

    static String randomString(int len){
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

}
