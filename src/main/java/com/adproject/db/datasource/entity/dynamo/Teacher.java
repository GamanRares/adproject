package com.adproject.db.datasource.entity.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.List;
import java.util.Objects;

@DynamoDBTable(tableName = "Teacher")
public class Teacher {

    @DynamoDBHashKey
    public Integer id;

    @DynamoDBAttribute
    public String surname;

    @DynamoDBAttribute
    public String forename;

    @DynamoDBAttribute
    public String description;

    @DynamoDBAttribute
    public Integer age;

    @DynamoDBAttribute
    public List<Integer> subjects;

    @DynamoDBAttribute
    public Address address;

    public Teacher() {
    }

    public Teacher(Integer id, String surname, String forename, String description, Integer age, List<Integer> subjects, Address address) {
        this.id = id;
        this.surname = surname;
        this.forename = forename;
        this.description = description;
        this.age = age;
        this.subjects = subjects;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<Integer> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Integer> subjects) {
        this.subjects = subjects;
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
        if (!(o instanceof Teacher)) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(id, teacher.id) &&
                Objects.equals(surname, teacher.surname) &&
                Objects.equals(forename, teacher.forename) &&
                Objects.equals(description, teacher.description) &&
                Objects.equals(age, teacher.age) &&
                Objects.equals(subjects, teacher.subjects) &&
                Objects.equals(address, teacher.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, surname, forename, description, age, subjects, address);
    }
}
