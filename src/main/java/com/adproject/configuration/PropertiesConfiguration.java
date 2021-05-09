package com.adproject.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;

/**
 * <i>Configuration</i> utility, used in order to load the data form .properties files
 * before initialization of other <i>Configuration</i> classes.
 *
 * @author Gaman Rares-Constantin on 5/9/21
 * Copyright © 2021 Gaman Rares-Constantin. All rights reserved.
 */
@Configuration
@PropertySource("classpath:application.properties")
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, MongoAutoConfiguration.class,
        MongoDataAutoConfiguration.class})
public class PropertiesConfiguration {

    public static String MY_SQL_URL;

    public static String MY_SQL_USERNAME;

    public static String MY_SQL_PASSWORD;

    public static String MONGODB_URL;

    public static String MONGODB_USERNAME;

    public static String MONGODB_PASSWORD;

    @Value("${datasource.my.sql.url}")
    private String mySQL_URL;

    @Value("${datasource.my.sql.username}")
    private String mySQL_Username;

    @Value("${datasource.my.sql.password}")
    private String mySQL_Password;

    @Value("${datasource.mongodb.url}")
    private String mongoDB_URL;

    @Value("${datasource.mongodb.username}")
    private String mongoDB_Username;

    @Value("${datasource.mongodb.password}")
    private String mongoDB_Password;

    @PostConstruct
    public void postConstruct() {
        PropertiesConfiguration.MY_SQL_URL = this.mySQL_URL;
        PropertiesConfiguration.MY_SQL_USERNAME = this.mySQL_Username;
        PropertiesConfiguration.MY_SQL_PASSWORD = this.mySQL_Password;
        PropertiesConfiguration.MONGODB_URL = this.mongoDB_URL;
        PropertiesConfiguration.MONGODB_USERNAME = this.mongoDB_Username;
        PropertiesConfiguration.MONGODB_PASSWORD = this.mongoDB_Password;
    }
}
