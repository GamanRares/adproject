package com.adproject.db.datasource.entity.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

@DynamoDBTable(tableName = "Subject")
public class Subject {

    @DynamoDBHashKey
    public Integer id;

    @DynamoDBAttribute
    public String name;

    @DynamoDBAttribute
    public Integer credits;

    @DynamoDBAttribute
    public String description;

    public Subject() {
    }

    public Subject(Integer id, String name, Integer credits, String description) {
        this.id = id;
        this.name = name;
        this.credits = credits;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subject)) return false;
        Subject subject = (Subject) o;
        return Objects.equals(id, subject.id) &&
                Objects.equals(name, subject.name) &&
                Objects.equals(credits, subject.credits) &&
                Objects.equals(description, subject.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, credits, description);
    }
}
