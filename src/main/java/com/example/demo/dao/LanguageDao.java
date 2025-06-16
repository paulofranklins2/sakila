package com.example.demo.dao;

import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class LanguageDao {
    private final DataSource dataSource;

    public LanguageDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<LanguageDao> finAll() {
        String sql = "select * from language";
        List<LanguageDao> languages = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                languages.add(new LanguageDao(
                        resultSet.getInt("language_id"),
                        resultSet.getString("name")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return languages;
    }
}
