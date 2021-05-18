package com.adproject.db.datasource.template;

import com.adproject.configuration.DynamoDBConfiguration;
import com.adproject.db.datasource.Template;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import org.springframework.context.annotation.Configuration;

/**
 * @author Gaman Rares-Constantin on 5/16/21
 * Copyright © 2021 Gaman Rares-Constantin. All rights reserved.
 */
@Configuration
public class DynamoDBTemplate extends DynamoDB implements Template {

    public DynamoDBTemplate() {
        super(DynamoDBConfiguration.AMAZON_DYNAMO_DB);
    }
}
