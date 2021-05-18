package com.adproject.service;

import com.adproject.db.datasource.Template;
import com.adproject.db.datasource.TemplateFactory;
import com.adproject.db.datasource.template.DynamoDBTemplate;
import com.adproject.db.datasource.template.MySQLTemplate;
import com.adproject.service.dynamo.DynamoServiceImpl;
import com.adproject.service.mongo.MongoServiceImpl;
import com.adproject.service.mysql.SqlServiceImpl;
import org.springframework.stereotype.Component;

/**
 * Factory class for all {@link Service}s
 *
 * @author Gaman Rares-Constantin on 5/18/21
 * Copyright © 2021 Gaman Rares-Constantin. All rights reserved.
 */
@Component
public class ServiceFactory {

    private static MongoServiceImpl mongoService;

    private static DynamoServiceImpl dynamoService;

    private static SqlServiceImpl sqlService;

    protected ServiceFactory(MongoServiceImpl mongoService, DynamoServiceImpl dynamoService, SqlServiceImpl sqlService) {
        ServiceFactory.mongoService = mongoService;
        ServiceFactory.dynamoService = dynamoService;
        ServiceFactory.sqlService = sqlService;
    }

    public static <T extends Service> T getService(Class<T> tClass) {
        T returnedClass = ServiceFactory.privateGetService(tClass);
        if (returnedClass == null)
            throw new NullPointerException("Service hasn't been initialized or it doesn't exists");

        return returnedClass;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Service> T privateGetService(Class<T> tClass) {
        switch (tClass.getName()) {
            case "com.adproject.service.mongo.MongoServiceImpl": {
                return (T) mongoService;
            }
            case "com.adproject.service.dynamo.DynamoServiceImpl": {
                return (T) dynamoService;
            }
            case "com.adproject.service.mysql.SqlServiceImpl": {
                return (T) sqlService;
            }
        }
        return null;
    }
}
