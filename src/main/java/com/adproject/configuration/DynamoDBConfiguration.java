package com.adproject.configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * @author Gaman Rares-Constantin on 5/16/21
 * Copyright © 2021 Gaman Rares-Constantin. All rights reserved.
 */
@Configuration
@PropertySource("classpath:application.properties")
public class DynamoDBConfiguration {

    public static AmazonDynamoDB AMAZON_DYNAMO_DB;

    public static DynamoDBMapper DYNAMO_DB_MAPPER;

    public static DynamoDBMapperConfig DYNAMO_DB_MAPPER_CONFIG = DynamoDBMapperConfig.DEFAULT;

    @Value("${datasource.amazon.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;

    @Value("${datasource.amazon.aws.accesskey}")
    private String amazonAWSAccessKey;

    @Value("${datasource.amazon.aws.secretkey}")
    private String amazonAWSSecretKey;

    @PostConstruct
    public void postConstruct() {
        DynamoDBConfiguration.AMAZON_DYNAMO_DB = this.amazonDynamoDB();
        DynamoDBConfiguration.DYNAMO_DB_MAPPER = new DynamoDBMapper(DynamoDBConfiguration.AMAZON_DYNAMO_DB);

    }

    private AmazonDynamoDB amazonDynamoDB() {
        AmazonDynamoDB amazonDynamoDB
                = new AmazonDynamoDBClient(amazonAWSCredentials());

        if (!StringUtils.isEmpty(amazonDynamoDBEndpoint)) {
            amazonDynamoDB.setEndpoint(amazonDynamoDBEndpoint);
        }

        return amazonDynamoDB;
    }

    private AWSCredentials amazonAWSCredentials() {
        return new BasicAWSCredentials(
                amazonAWSAccessKey, amazonAWSSecretKey);
    }

}
