package com.adproject.db.datasource.template;

import com.adproject.db.datasource.Template;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class MySQLTemplate extends JdbcTemplate implements Template {
    public MySQLTemplate() {
        super(DataSourceBuilder.create()
                .url("jdbc:mysql://localhost:3306/ad_project?serverTimezone=UTC")
                .username("root")
                .password("administrator")
                .build());
    }
}