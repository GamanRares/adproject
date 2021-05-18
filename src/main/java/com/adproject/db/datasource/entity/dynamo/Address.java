package com.adproject.db.datasource.entity.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

@DynamoDBTable(tableName = "Address")
public class Address {

    @DynamoDBHashKey
    public Integer oid;

    @DynamoDBAttribute
    public String street;

    @DynamoDBAttribute
    public String zip_code;

    public Address() {
    }

    public Address(Integer oid, String street, String zip_code) {
        this.oid = oid;
        this.street = street;
        this.zip_code = zip_code;
    }

    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;
        Address address = (Address) o;
        return Objects.equals(street, address.street) &&
                Objects.equals(zip_code, address.zip_code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, zip_code);
    }
}
