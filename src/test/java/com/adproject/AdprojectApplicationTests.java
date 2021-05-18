package com.adproject;

import com.adproject.configuration.DynamoDBConfiguration;
import com.adproject.db.datasource.TemplateFactory;
import com.adproject.db.datasource.entity.dynamo.*;
import com.adproject.db.datasource.entity.mongo.Student;
import com.adproject.db.datasource.template.DynamoDBTemplate;
import com.adproject.db.datasource.template.MySQLTemplate;
import com.adproject.service.dynamo.DynamoServiceImpl;
import com.adproject.service.mongo.MongoServiceImpl;
import com.adproject.service.mysql.SqlServiceImpl;
import com.adproject.utils.ExecutionTime;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.adproject.db.datasource.TemplateFactory.getTemplate;
import static com.adproject.db.datasource.entity.dynamo.TableNames.FACULTY;
import static com.adproject.db.datasource.entity.dynamo.TableNames.STUDENT;

@SpringBootTest
@RunWith(SpringRunner.class)
class AdprojectApplicationTests {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    MongoServiceImpl mongoService;

    @Autowired
    DynamoServiceImpl dynamoService;

    @Autowired
    SqlServiceImpl sqlService;

    @Test
    void contextLoads() {
        final String sqlRowSet = TemplateFactory.getTemplate(MySQLTemplate.class).queryForObject("SELECT s.surname FROM Student s WHERE s.id = 14", String.class);
        System.out.println(sqlRowSet);
    }

    @Test
    void testMongoDBConnection() {
        List<Student> faculties = mongoTemplate.findAll(Student.class);
        System.out.println(faculties.size());
    }

    @Test
    void testMongoQueryNo1() {
        ExecutionTime executionTime = mongoService.executeQueryNo1();
        System.out.println(executionTime);
    }

    @Test
    void testSQLQueryNo1() {
        ExecutionTime executionTime = sqlService.executeQueryNo1();
        System.out.println(executionTime);
    }

    @Test
    void testDynamoDBQueryNo1() {
        ExecutionTime executionTime = dynamoService.executeQueryNo1();
        System.out.println(executionTime);
    }

    @Test
    void testMongoQueryNo2() {
        ExecutionTime executionTime = mongoService.executeQueryNo2();
        System.out.println(executionTime);
    }

    @Test
    void testSQLQueryNo2() {
        ExecutionTime executionTime = sqlService.executeQueryNo2();
        System.out.println(executionTime);
    }

    @Test
    void testDynamoDBQueryNo2() {
        ExecutionTime executionTime = dynamoService.executeQueryNo2();
        System.out.println(executionTime);
    }

    @Test
    void testMongoQueryNo3() {
        ExecutionTime executionTime = mongoService.executeQueryNo3();
        System.out.println(executionTime);
    }

    @Test
    void testSQLQueryNo3() {
        ExecutionTime executionTime = sqlService.executeQueryNo3();
        System.out.println(executionTime);
    }

    @Test
    void testDynamoDBQueryNo3() {
        ExecutionTime executionTime = dynamoService.executeQueryNo3();
        System.out.println(executionTime);
    }

    @Test
    void testMongoQueryNo4() {
        ExecutionTime executionTime = mongoService.executeQueryNo4();
        System.out.println(executionTime);
    }

    @Test
    void testSQLQueryNo4() {
        ExecutionTime executionTime = sqlService.executeQueryNo4();
        System.out.println(executionTime);
    }

    @Test
    void testDynamoDBQueryNo4() {
        ExecutionTime executionTime = dynamoService.executeQueryNo4();
        System.out.println(executionTime);
    }

    @Test
    void testMongoQueryNo5() {
        ExecutionTime executionTime = mongoService.executeQueryNo5();
        System.out.println(executionTime);
    }

    @Test
    void testSQLQueryNo5() {
        ExecutionTime executionTime = sqlService.executeQueryNo5();
        System.out.println(executionTime);
    }

    @Test
    void testDynamoDBQueryNo5() {
        ExecutionTime executionTime = dynamoService.executeQueryNo5();
        System.out.println(executionTime);
    }

    @Test
    void testMongoQueryNo6() {
        ExecutionTime executionTime = mongoService.executeQueryNo6();
        System.out.println(executionTime);
    }

    @Test
    void testSQLQueryNo6() {
        ExecutionTime executionTime = sqlService.executeQueryNo6();
        System.out.println(executionTime);
    }

    @Test
    void testDynamoDBQueryNo6() {
        ExecutionTime executionTime = dynamoService.executeQueryNo6();
        System.out.println(executionTime);
    }

    @Test
    void testMongoQueryNo7() {
        ExecutionTime executionTime = mongoService.executeQueryNo7();
        System.out.println(executionTime);
    }

    @Test
    void testSQLQueryNo7() {
        ExecutionTime executionTime = sqlService.executeQueryNo7();
        System.out.println(executionTime);
    }

    @Test
    void testDynamoDBQueryNo7() {
        ExecutionTime executionTime = dynamoService.executeQueryNo7();
        System.out.println(executionTime);
    }

    @Test
    void testMongoQueryNo8() {
        ExecutionTime executionTime = mongoService.executeQueryNo8();
        System.out.println(executionTime);
    }

    @Test
    void testSQLQueryNo8() {
        ExecutionTime executionTime = sqlService.executeQueryNo8();
        System.out.println(executionTime);
    }

    @Test
    void testDynamoDBQueryNo8() {
        ExecutionTime executionTime = dynamoService.executeQueryNo8();
        System.out.println(executionTime);
    }

    @Test
    void testMongoQueryNo9() {
        ExecutionTime executionTime = mongoService.executeQueryNo9();
        System.out.println(executionTime);
    }

    @Test
    void testSQLQueryNo9() {
        ExecutionTime executionTime = sqlService.executeQueryNo9();
        System.out.println(executionTime);
    }

    @Test
    void testDynamoDBQueryNo9() {
        ExecutionTime executionTime = dynamoService.executeQueryNo9();
        System.out.println(executionTime);
    }

    @Test
    void testMongoQueryNo10() {
        ExecutionTime executionTime = mongoService.executeQueryNo10();
        System.out.println(executionTime);
    }

    @Test
    void testSQLQueryNo10() {
        ExecutionTime executionTime = sqlService.executeQueryNo10();
        System.out.println(executionTime);
    }

    @Test
    void testDynamoDBQueryNo10() {
        ExecutionTime executionTime = dynamoService.executeQueryNo10();
        System.out.println(executionTime);
    }

    //TODO Get all students together with address, city, ...

    //TODO Get all teachers of a student

    //TODO Get all students from a given city having a given teacher

    //TODO Find which department from a given faculty has the most credits

    //TODO Find the university with the largest number of students

    //TODO Find the university with the largest number of students at a course

    //TODO Find the oldest teacher from a given country

    //TODO Find the faculty with the highest student age mean

    //TODO Find all the students from a given country, studying a course with the least number of credits of that teacher

    //TODO Find the faculty with the least number of departments, but with the most students

    //between(LocalDate.of(1989, Month.OCTOBER, 14), LocalDate.now()
    public static LocalDate between(LocalDate startInclusive, LocalDate endExclusive) {
        long startEpochDay = startInclusive.toEpochDay();
        long endEpochDay = endExclusive.toEpochDay();
        long randomDay = ThreadLocalRandom.current()
                .nextLong(startEpochDay, endEpochDay);

        return LocalDate.ofEpochDay(randomDay);
    }

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static Random rnd = new Random();

    static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    void test111() {
        {
            DeleteTableRequest delete = DynamoDBConfiguration.DYNAMO_DB_MAPPER.generateDeleteTableRequest(Address.class);
            DynamoDBConfiguration.AMAZON_DYNAMO_DB.deleteTable(delete);

            CreateTableRequest addressTableRequest = DynamoDBConfiguration.DYNAMO_DB_MAPPER.generateCreateTableRequest(Address.class);
            addressTableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
            DynamoDBConfiguration.AMAZON_DYNAMO_DB.createTable(addressTableRequest);
        }
        {
            DeleteTableRequest delete = DynamoDBConfiguration.DYNAMO_DB_MAPPER.generateDeleteTableRequest(City.class);
            DynamoDBConfiguration.AMAZON_DYNAMO_DB.deleteTable(delete);

            CreateTableRequest cityTableRequest = DynamoDBConfiguration.DYNAMO_DB_MAPPER.generateCreateTableRequest(City.class);
            cityTableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
            DynamoDBConfiguration.AMAZON_DYNAMO_DB.createTable(cityTableRequest);
        }
        {
            DeleteTableRequest delete = DynamoDBConfiguration.DYNAMO_DB_MAPPER.generateDeleteTableRequest(Country.class);
            DynamoDBConfiguration.AMAZON_DYNAMO_DB.deleteTable(delete);

            CreateTableRequest countryTableRequest = DynamoDBConfiguration.DYNAMO_DB_MAPPER.generateCreateTableRequest(Country.class);
            countryTableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
            DynamoDBConfiguration.AMAZON_DYNAMO_DB.createTable(countryTableRequest);
        }
        {
            DeleteTableRequest delete = DynamoDBConfiguration.DYNAMO_DB_MAPPER.generateDeleteTableRequest(Department.class);
            DynamoDBConfiguration.AMAZON_DYNAMO_DB.deleteTable(delete);

            CreateTableRequest departmentTableRequest = DynamoDBConfiguration.DYNAMO_DB_MAPPER.generateCreateTableRequest(Department.class);
            departmentTableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
            DynamoDBConfiguration.AMAZON_DYNAMO_DB.createTable(departmentTableRequest);
        }
        {
            DeleteTableRequest delete = DynamoDBConfiguration.DYNAMO_DB_MAPPER.generateDeleteTableRequest(Faculty.class);
            DynamoDBConfiguration.AMAZON_DYNAMO_DB.deleteTable(delete);

            CreateTableRequest facultyTableRequest = DynamoDBConfiguration.DYNAMO_DB_MAPPER.generateCreateTableRequest(Faculty.class);
            facultyTableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
            DynamoDBConfiguration.AMAZON_DYNAMO_DB.createTable(facultyTableRequest);
        }
        {
            DeleteTableRequest delete = DynamoDBConfiguration.DYNAMO_DB_MAPPER.generateDeleteTableRequest(com.adproject.db.datasource.entity.dynamo.Student.class);
            DynamoDBConfiguration.AMAZON_DYNAMO_DB.deleteTable(delete);

            CreateTableRequest studentTableRequest = DynamoDBConfiguration.DYNAMO_DB_MAPPER.generateCreateTableRequest(com.adproject.db.datasource.entity.dynamo.Student.class);
            studentTableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
            DynamoDBConfiguration.AMAZON_DYNAMO_DB.createTable(studentTableRequest);
        }
        {
            DeleteTableRequest delete = DynamoDBConfiguration.DYNAMO_DB_MAPPER.generateDeleteTableRequest(Subject.class);
            DynamoDBConfiguration.AMAZON_DYNAMO_DB.deleteTable(delete);

            CreateTableRequest subjectTableRequest = DynamoDBConfiguration.DYNAMO_DB_MAPPER.generateCreateTableRequest(Subject.class);
            subjectTableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
            DynamoDBConfiguration.AMAZON_DYNAMO_DB.createTable(subjectTableRequest);
        }
        {
            DeleteTableRequest delete = DynamoDBConfiguration.DYNAMO_DB_MAPPER.generateDeleteTableRequest(Teacher.class);
            DynamoDBConfiguration.AMAZON_DYNAMO_DB.deleteTable(delete);

            CreateTableRequest teacherTableRequest = DynamoDBConfiguration.DYNAMO_DB_MAPPER.generateCreateTableRequest(Teacher.class);
            teacherTableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
            DynamoDBConfiguration.AMAZON_DYNAMO_DB.createTable(teacherTableRequest);
        }
        {
            DeleteTableRequest delete = DynamoDBConfiguration.DYNAMO_DB_MAPPER.generateDeleteTableRequest(University.class);
            DynamoDBConfiguration.AMAZON_DYNAMO_DB.deleteTable(delete);

            CreateTableRequest universityTableRequest = DynamoDBConfiguration.DYNAMO_DB_MAPPER.generateCreateTableRequest(University.class);
            universityTableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
            DynamoDBConfiguration.AMAZON_DYNAMO_DB.createTable(universityTableRequest);
        }
    }

    void createDynamoDbTables() {
        {
            CreateTableRequest addressTableRequest = DynamoDBConfiguration.DYNAMO_DB_MAPPER.generateCreateTableRequest(Address.class);
            addressTableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
            DynamoDBConfiguration.AMAZON_DYNAMO_DB.createTable(addressTableRequest);
        }
        {
            CreateTableRequest cityTableRequest = DynamoDBConfiguration.DYNAMO_DB_MAPPER.generateCreateTableRequest(City.class);
            cityTableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
            DynamoDBConfiguration.AMAZON_DYNAMO_DB.createTable(cityTableRequest);
        }
        {
            CreateTableRequest countryTableRequest = DynamoDBConfiguration.DYNAMO_DB_MAPPER.generateCreateTableRequest(Country.class);
            countryTableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
            DynamoDBConfiguration.AMAZON_DYNAMO_DB.createTable(countryTableRequest);
        }
        {
            CreateTableRequest departmentTableRequest = DynamoDBConfiguration.DYNAMO_DB_MAPPER.generateCreateTableRequest(Department.class);
            departmentTableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
            DynamoDBConfiguration.AMAZON_DYNAMO_DB.createTable(departmentTableRequest);
        }
        {
            CreateTableRequest facultyTableRequest = DynamoDBConfiguration.DYNAMO_DB_MAPPER.generateCreateTableRequest(Faculty.class);
            facultyTableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
            DynamoDBConfiguration.AMAZON_DYNAMO_DB.createTable(facultyTableRequest);
        }
        {
            CreateTableRequest studentTableRequest = DynamoDBConfiguration.DYNAMO_DB_MAPPER.generateCreateTableRequest(com.adproject.db.datasource.entity.dynamo.Student.class);
            studentTableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
            DynamoDBConfiguration.AMAZON_DYNAMO_DB.createTable(studentTableRequest);
        }
        {
            CreateTableRequest subjectTableRequest = DynamoDBConfiguration.DYNAMO_DB_MAPPER.generateCreateTableRequest(Subject.class);
            subjectTableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
            DynamoDBConfiguration.AMAZON_DYNAMO_DB.createTable(subjectTableRequest);
        }
        {
            CreateTableRequest teacherTableRequest = DynamoDBConfiguration.DYNAMO_DB_MAPPER.generateCreateTableRequest(Teacher.class);
            teacherTableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
            DynamoDBConfiguration.AMAZON_DYNAMO_DB.createTable(teacherTableRequest);
        }
        {
            CreateTableRequest universityTableRequest = DynamoDBConfiguration.DYNAMO_DB_MAPPER.generateCreateTableRequest(University.class);
            universityTableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
            DynamoDBConfiguration.AMAZON_DYNAMO_DB.createTable(universityTableRequest);
        }
    }

    void fillDynamoDbTables() throws JSONException {
        {
            Table countryTable = TemplateFactory.getTemplate(DynamoDBTemplate.class).getTable(TableNames.COUNTRY.getName());
            for (int i = 1; i<=10000; i++) {
                Item item = new Item().withPrimaryKey("id", i)
                        .withString("name", randomString(3))
                        .withInt("population", ThreadLocalRandom.current().nextInt(0, 10000));
                countryTable.putItem(item);
            }
        }
        {
            Table cityTable = TemplateFactory.getTemplate(DynamoDBTemplate.class).getTable(TableNames.CITY.getName());
            for (int i = 1; i <= 10000; i++) {
                Item item = new Item().withPrimaryKey("oid", i)
                        .withInt("tid", i)
                        .withString("name", randomString(3))
                        .withString("attractions", randomString(3));
                cityTable.putItem(item);
            }
        }
        {
            Table addressTable = TemplateFactory.getTemplate(DynamoDBTemplate.class).getTable(TableNames.ADDRESS.getName());
            for (int i = 1; i <= 10000; i++) {
                Item item = new Item().withPrimaryKey("oid", i)
                        .withString("street", randomString(3))
                        .withString("zip_code", randomString(3));
                addressTable.putItem(item);
            }
        }
        {
            Table departmentTable = TemplateFactory.getTemplate(DynamoDBTemplate.class).getTable(TableNames.DEPARTMENT.getName());
            for (int i = 1; i <= 10000; i++) {
                Item item = new Item().withPrimaryKey("name", randomString(3))
                        .withString("description", randomString(3))
                        .withList("subjects", ThreadLocalRandom.current().ints(1, 10000).distinct().limit(10).boxed().collect(Collectors.toList()));
                departmentTable.putItem(item);
            }
        }
        {
            Table studentTable = TemplateFactory.getTemplate(DynamoDBTemplate.class).getTable(TableNames.STUDENT.getName());
            for (int i = 1; i <= 10000; i++) {
                Item item = new Item().withPrimaryKey("id", i)
                        .withInt("age", ThreadLocalRandom.current().nextInt(0, 100))
                        .withString("firstname", randomString(3))
                        .withString("surname", randomString(3))
                        .withJSON("address", new JSONObject()
                                .put("street", randomString(3))
                                .put("zip_code", randomString(3))
                                .toString());
                studentTable.putItem(item);
            }
        }
        {
            Table subjectTable = TemplateFactory.getTemplate(DynamoDBTemplate.class).getTable(TableNames.SUBJECT.getName());
            for (int i = 1; i <= 10000; i++) {
                Item item = new Item().withPrimaryKey("id", i)
                        .withString("name", randomString(3))
                        .withInt("credits", ThreadLocalRandom.current().nextInt(0, 10))
                        .withString("description", randomString(3));
                subjectTable.putItem(item);
            }
        }
        {
            Table teacherTable = TemplateFactory.getTemplate(DynamoDBTemplate.class).getTable(TableNames.TEACHER.getName());
            for (int i = 1; i <= 10000; i++) {
                Item item = new Item().withPrimaryKey("id", i)
                        .withString("surname", randomString(3))
                        .withString("forename", randomString(3))
                        .withString("description", randomString(3))
                        .withInt("age", ThreadLocalRandom.current().nextInt(0, 10))
                        .withString("description", randomString(3))
                        .withList("subjects", ThreadLocalRandom.current().ints(1, 10000).distinct().limit(10).boxed().collect(Collectors.toList()))
                        .withJSON("address", new JSONObject()
                                .put("street", randomString(3))
                                .put("zip_code", randomString(3))
                                .toString());
                teacherTable.putItem(item);
            }
        }
        {
            Table universityTable = TemplateFactory.getTemplate(DynamoDBTemplate.class).getTable(TableNames.UNIVERSITY.getName());
            for (int i = 1; i <= 10000; i++) {
                Item item = new Item().withPrimaryKey("uid", i)
                        .withString("name", randomString(3))
                        .withString("foundation_year", between(LocalDate.of(1989, Month.OCTOBER, 14), LocalDate.now()).toString());
                universityTable.putItem(item);
            }
        }
        {
            Table facultyTable = TemplateFactory.getTemplate(DynamoDBTemplate.class).getTable(TableNames.FACULTY.getName());
            for (int i = 1; i <= 10000; i++) {
                List<String> departments = new ArrayList<>();
                for (int j = 0; j<= 10; j++) {
                    JSONArray jsonArray = new JSONArray();
                    for (int z = 0; z<= 10; z++) {
                        jsonArray.put(ThreadLocalRandom.current().nextInt(0, 10000));
                    }
                    departments.add(new JSONObject()
                            .put("name", randomString(3))
                            .put("description", randomString(3))
                            .put("subjects", jsonArray)
                            .toString());
                }
                Item item = new Item().withPrimaryKey("id", i)
                        .withInt("uid", i)
                        .withString("name", randomString(3))
                        .withString("foundation_year", between(LocalDate.of(1989, Month.OCTOBER, 14), LocalDate.now()).toString())
                        .withList("students", ThreadLocalRandom.current().ints(1, 10000).distinct().limit(10).boxed().collect(Collectors.toList()))
                        .withList("teachers", ThreadLocalRandom.current().ints(1, 10000).distinct().limit(10).boxed().collect(Collectors.toList()))
                        .withJSON("address", new JSONObject()
                                .put("street", randomString(3))
                                .put("zip_code", randomString(3))
                                .toString())
                        .withList("departments", departments);
                facultyTable.putItem(item);
            }
        }
    }
}
