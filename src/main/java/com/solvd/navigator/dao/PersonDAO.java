package com.solvd.navigator.dao;

import com.solvd.navigator.bin.Person;
import com.solvd.navigator.util.DBConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonDAO implements AbstractDAO<Person> {
    private static final Logger LOGGER = (Logger) LogManager.getLogger(PersonDAO.class);

    private static final String CREATE_PERSON = "INSERT INTO persons (person_id, first_name, last_name)" + "VALUES (?,?,?)";
    private static final String GET_BY_ID_QUERY = "SELECT * FROM persons WHERE person_id = ?";
    private static final String UPDATE_QUERY = "UPDATE persons SET first_name=?, last_name=? WHERE person_id=?";
    private static final String DELETE_QUERY = "DELETE FROM persons WHERE person_id=?";

    private Connection dbConnection;


    @Override
    public int create(Person person) {
        try (Connection dbConnection = DBConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(CREATE_PERSON)) {
            preparedStatement.setInt(1, person.getPersonId());
            preparedStatement.setString(2, person.getFirstName());
            preparedStatement.setString(3,person.getLastName());
            LOGGER.info("Row inserted into db");
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnectionPool.getInstance().releaseConnection(dbConnection);
        }
    }

    @Override
    public Person getById(int personId) {
        Person person = null;
        try (Connection dbConnection = DBConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(GET_BY_ID_QUERY)) {
            preparedStatement.setInt(1, personId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // If a record is found, create a Person object
                    person = new Person(
                            resultSet.getInt("person_id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting person from the database", e);
        } finally {
            DBConnectionPool.getInstance().releaseConnection(dbConnection);
        }
        return person;
    }

    @Override
    public void update(Person person) {
        try (Connection dbConnection = DBConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(UPDATE_QUERY)) {
            preparedStatement.setString(1, person.getFirstName());
            preparedStatement.setString(2, person.getLastName());
            preparedStatement.setInt(3, person.getPersonId());
            LOGGER.info("Row updated in DB: Person ID - " + person.getPersonId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating person in the database", e);
        } finally {
            DBConnectionPool.getInstance().releaseConnection(dbConnection);
        }
    }

    @Override
    public void delete(int personId) {
        try (Connection dbConnection = DBConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(DELETE_QUERY)) {
            preparedStatement.setInt(1, personId);
            LOGGER.info("Row deleted from DB: Person ID - " + personId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting person from the database", e);
        } finally {
            DBConnectionPool.getInstance().releaseConnection(dbConnection);
        }
    }
}
