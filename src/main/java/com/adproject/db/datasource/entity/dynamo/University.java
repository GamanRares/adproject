package com.adproject.db.datasource.entity.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

@DynamoDBTable(tableName = "University")
public class University {

    @DynamoDBHashKey
    public Integer uid;

    @DynamoDBAttribute
    public String name;

    @DynamoDBAttribute
    public String foundation_year;

    public University() {
    }

    public University(Integer uid, String name, String foundation_year) {
        this.uid = uid;
        this.name = name;
        this.foundation_year = foundation_year;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFoundation_year() {
        return foundation_year;
    }

    public void setFoundation_year(String foundation_year) {
        this.foundation_year = foundation_year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof University)) return false;
        University that = (University) o;
        return Objects.equals(uid, that.uid) &&
                Objects.equals(name, that.name) &&
                Objects.equals(foundation_year, that.foundation_year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, name, foundation_year);
    }
}
