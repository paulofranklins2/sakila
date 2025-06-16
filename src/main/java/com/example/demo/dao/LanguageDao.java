package com.example.demo.dao;

import com.example.demo.model.Language;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class LanguageDao {
    private final DataSource dataSource;

    public LanguageDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Language> findAll() {
        String sql = "SELECT * FROM language";
        List<Language> languages = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                languages.add(new Language(
                        resultSet.getInt("language_id"),
                        resultSet.getString("name")));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching languages: " + e.getMessage());
        }

        return languages;
    }


    public Language finById(int id) {
        String sql = "select * from language where language_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Language(
                            resultSet.getInt("language_id"),
                            resultSet.getString("name"));
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
