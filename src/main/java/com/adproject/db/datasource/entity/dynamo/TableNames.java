package com.adproject.db.datasource.entity.dynamo;

/**
 * @author Gaman Rares-Constantin on 5/17/21
 * Copyright © 2021 Gaman Rares-Constantin. All rights reserved.
 */
public enum TableNames {
    ADDRESS, CITY, COUNTRY, DEPARTMENT, FACULTY, STUDENT, SUBJECT, TEACHER, UNIVERSITY;

    public String getName() {
        return capitalize(this.name());
    }

    private static String capitalize(String str) {
        if(str == null || str.isEmpty()) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
