package com.adproject.db.datasource.entity.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

@DynamoDBTable(tableName = "Student")
public class Student {

    @DynamoDBHashKey
    public Integer id;

    @DynamoDBAttribute
    public Integer age;

    @DynamoDBAttribute
    public String firstname;

    @DynamoDBAttribute
    public String surname;

    @DynamoDBAttribute
    public Address address;

    public Student() {
    }

    public Student(Integer id, Integer age, String firstname, String surname, Address address) {
        this.id = id;
        this.age = age;
        this.firstname = firstname;
        this.surname = surname;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id) &&
                Objects.equals(age, student.age) &&
                Objects.equals(firstname, student.firstname) &&
                Objects.equals(surname, student.surname) &&
                Objects.equals(address, student.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, age, firstname, surname, address);
    }
}
