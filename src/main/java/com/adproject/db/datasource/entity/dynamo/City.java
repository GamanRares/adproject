package com.adproject.db.datasource.entity.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

@DynamoDBTable(tableName = "City")
public class City {

    @DynamoDBHashKey
    public Integer oid;

    @DynamoDBAttribute
    public Integer tid;

    @DynamoDBAttribute
    public String name;

    @DynamoDBAttribute
    public String attractions;

    public City() {
    }

    public City(Integer oid, Integer tid, String name, String attractions) {
        this.oid = oid;
        this.tid = tid;
        this.name = name;
        this.attractions = attractions;
    }

    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttractions() {
        return attractions;
    }

    public void setAttractions(String attractions) {
        this.attractions = attractions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City)) return false;
        City city = (City) o;
        return Objects.equals(oid, city.oid) &&
                Objects.equals(tid, city.tid) &&
                Objects.equals(name, city.name) &&
                Objects.equals(attractions, city.attractions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oid, tid, name, attractions);
    }
}
