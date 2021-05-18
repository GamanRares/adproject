package com.adproject.db.datasource.entity.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.List;
import java.util.Objects;

@DynamoDBTable(tableName = "Faculty")
public class Faculty {

    @DynamoDBHashKey
    public Integer id;

    @DynamoDBAttribute
    public Integer uid;

    @DynamoDBAttribute
    public String name;

    @DynamoDBAttribute
    public String foundation_year;

    @DynamoDBAttribute
    public List<Integer> students;

    @DynamoDBAttribute
    public List<Integer> teachers;

    @DynamoDBAttribute
    public Address address;

    @DynamoDBAttribute
    public List<Department> departments;

    public Faculty() {
    }

    public Faculty(Integer id, Integer uid, String name, String foundation_year, List<Integer> students, List<Integer> teachers, Address address, List<Department> departments) {
        this.id = id;
        this.uid = uid;
        this.name = name;
        this.foundation_year = foundation_year;
        this.students = students;
        this.teachers = teachers;
        this.address = address;
        this.departments = departments;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public List<Integer> getStudents() {
        return students;
    }

    public void setStudents(List<Integer> students) {
        this.students = students;
    }

    public List<Integer> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Integer> teachers) {
        this.teachers = teachers;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Faculty)) return false;
        Faculty faculty = (Faculty) o;
        return Objects.equals(id, faculty.id) &&
                Objects.equals(uid, faculty.uid) &&
                Objects.equals(name, faculty.name) &&
                Objects.equals(foundation_year, faculty.foundation_year) &&
                Objects.equals(students, faculty.students) &&
                Objects.equals(teachers, faculty.teachers) &&
                Objects.equals(address, faculty.address) &&
                Objects.equals(departments, faculty.departments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uid, name, foundation_year, students, teachers, address, departments);
    }
}
