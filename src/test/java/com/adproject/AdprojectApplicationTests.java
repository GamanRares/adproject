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

}
