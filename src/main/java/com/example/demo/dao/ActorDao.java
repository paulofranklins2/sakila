package com.example.demo.dao;

import com.example.demo.model.Actor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ActorDao {
    private final DataSource dataSource;

    public ActorDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Actor> findAll(int page) {
        String sql = "select * from actor LIMIT ? OFFSET ?";
        List<Actor> actors = new ArrayList<>();
        final int MAX_ACTORS_PER_PAGE = 20;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, MAX_ACTORS_PER_PAGE);
            preparedStatement.setInt(2, page * MAX_ACTORS_PER_PAGE);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    actors.add(new Actor(
                            resultSet.getInt("actor_id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name")
                    ));
                }

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return actors;
    }

    public Actor findById(int id) {
        String sql = "select * from actor where actor_id = ?";
        Actor actor = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    actor = new Actor(
                            resultSet.getInt("actor_id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return actor;
    }

    public Actor findByFirstName(String firstName) {
        String sql = "select * from actor where first_name = ?";
        Actor actor = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, firstName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    actor = new Actor(
                            resultSet.getInt("actor_id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return actor;
    }

    public Actor save(Actor actor) {
        String sql = "insert into actor (first_name, last_name) values (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, actor.getFirstName());
            preparedStatement.setString(2, actor.getLastName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return actor;
    }

    public boolean update(int id, Actor actor) {
        Actor byId = findById(id);
        if (byId == null) return false;

        boolean isFirstNameChanged = !actor.getFirstName().isBlank() && !byId.getFirstName().equals(actor.getFirstName());
        boolean isLastNameChanged = !actor.getLastName().isBlank() && !byId.getLastName().equals(actor.getLastName());

        if (!isFirstNameChanged && !isLastNameChanged) return false;

        String sql = "UPDATE actor SET first_name = ?, last_name = ? WHERE actor_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, isFirstNameChanged ? actor.getFirstName() : byId.getFirstName());
            preparedStatement.setString(2, isLastNameChanged ? actor.getLastName() : byId.getLastName());
            preparedStatement.setInt(3, id);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update actor.html with id " + id, e);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM actor WHERE actor_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete actor.html with id " + id, e);
        }
    }
}
