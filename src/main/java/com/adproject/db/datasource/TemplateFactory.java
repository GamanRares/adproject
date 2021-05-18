package com.adproject.db.datasource;

import com.adproject.db.datasource.template.DynamoDBTemplate;
import com.adproject.db.datasource.template.MySQLTemplate;
import org.springframework.stereotype.Component;

/**
 * Factory class for all database {@link Template}s
 *
 * @author Gaman Rares-Constantin on 4/21/21
 * Copyright © 2021 Gaman Rares-Constantin. All rights reserved.
 */
@Component
public class TemplateFactory {

    private static MySQLTemplate mySQLTemplate;

    private static DynamoDBTemplate dynamoDBTemplate;

    protected TemplateFactory(MySQLTemplate mySQLTemplate, DynamoDBTemplate dynamoDBTemplate) {
        TemplateFactory.mySQLTemplate = mySQLTemplate;
        TemplateFactory.dynamoDBTemplate = dynamoDBTemplate;
    }

    public static <T extends Template> T getTemplate(Class<T> tClass) {
        T returnedClass = TemplateFactory.privateGetTemplate(tClass);
        if (returnedClass == null)
            throw new NullPointerException("Database Template hasn't been initialized or it doesn't exists");

        return returnedClass;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Template> T privateGetTemplate(Class<T> tClass) {
        switch (tClass.getName()) {
            case "com.adproject.db.datasource.template.MySQLTemplate": {
                return (T) mySQLTemplate;
            }
            case "com.adproject.db.datasource.template.DynamoDBTemplate": {
                return (T) dynamoDBTemplate;
            }
        }
        return null;
    }
}
