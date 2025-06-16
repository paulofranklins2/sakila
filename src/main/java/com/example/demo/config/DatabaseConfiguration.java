package com.example.demo.config;


import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration {

    @Bean
    public DataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mariadb://localhost:3306/sakila");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        return dataSource;
    }
}
