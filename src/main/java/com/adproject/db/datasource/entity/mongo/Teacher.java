package com.adproject.db.datasource.entity.mongo;

import java.util.List;

public class Teacher {
    public Integer id;
    public String surname;
    public String forname;
    public String description;
    public Integer age;
    public List<Integer> subjects;
    public Address address;

}
