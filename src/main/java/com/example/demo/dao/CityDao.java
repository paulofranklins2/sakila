package com.example.demo.dao;

import com.example.demo.config.DatabaseConfiguration;
import com.example.demo.model.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CityDao {
    private final DataSource dataSource;

    @Autowired
    public CityDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /*
     * Get ALL from @City
     */
    public List<City> findAll() {
        String sql = "select * from city";
        List<City> cities = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                cities.add(new City(
                        resultSet.getInt("city_id"),
                        resultSet.getString("city"),
                        resultSet.getInt("country_id")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return cities;
    }

    /*
     * Get @City by ID;
     */
    public City findById(int id) {
        String sql = "SELECT * FROM city WHERE city_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id); // Set parameter correctly

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new City(
                            resultSet.getInt("city_id"),
                            resultSet.getString("city"),
                            resultSet.getInt("country_id")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage()); // Consider logging in production
        }
        return null;
    }

}
