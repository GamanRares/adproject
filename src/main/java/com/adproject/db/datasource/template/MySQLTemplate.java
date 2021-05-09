package com.adproject.db.datasource.template;

import com.adproject.configuration.PropertiesConfiguration;
import com.adproject.db.datasource.Template;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class MySQLTemplate extends JdbcTemplate implements Template {

    public MySQLTemplate() {
        super(DataSourceBuilder.create()
                .url(PropertiesConfiguration.MY_SQL_URL)
                .username(PropertiesConfiguration.MY_SQL_USERNAME)
                .password(PropertiesConfiguration.MY_SQL_PASSWORD)
                .build());
    }
}