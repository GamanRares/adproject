package com.adproject;

import com.adproject.db.datasource.TemplateFactory;
import com.adproject.db.datasource.template.MySQLTemplate;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class AdprojectApplicationTests {

    @Test
    void contextLoads() {
        final String sqlRowSet = TemplateFactory.getTemplate(MySQLTemplate.class).queryForObject("SELECT s.surname FROM Student s WHERE s.id = 14", String.class);
        System.out.println(sqlRowSet);
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

}
