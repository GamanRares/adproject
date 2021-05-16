package com.adproject.db.datasource.entity.mongo;

import java.util.List;

public class Faculty {
    public Integer id;
    public Integer uid;
    public String name;
    public String foundation_year;
    public List<Integer> students;
    public List<Integer> teachers;
    Address address;
    public List<Department> departments;

}
