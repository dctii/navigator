package com.solvd.navigator.dao.jdbc;

import com.solvd.navigator.bin.Person;
import com.solvd.navigator.dao.PersonDAO;
import com.solvd.navigator.util.DBConnectionPool;
import com.solvd.navigator.util.SQLUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class PersonJDBCImpl implements PersonDAO {
    private static final Logger LOGGER = LogManager.getLogger(PersonJDBCImpl.class);

    private static final String CREATE_PERSON = "INSERT INTO persons (first_name, last_name)" + "VALUES (?,?)";
    private static final String GET_BY_ID_QUERY = "SELECT * FROM persons WHERE person_id = ?";
    private static final String UPDATE_QUERY = "UPDATE persons SET first_name=?, last_name=? WHERE person_id=?";
    private static final String DELETE_QUERY = "DELETE FROM persons WHERE person_id=?";
    private final DBConnectionPool connectionPool = DBConnectionPool.getInstance();



    @Override
    public int create(Person person) {
        Connection dbConnection = connectionPool.getConnection();
        int newPersonId = 0;
        try (
             PreparedStatement preparedStatement = dbConnection.prepareStatement(
                     CREATE_PERSON,
                     Statement.RETURN_GENERATED_KEYS)
        ) {
            SQLUtils.setStringOrNull(preparedStatement,1, person.getFirstName());
            SQLUtils.setStringOrNull(preparedStatement,2,person.getLastName());
            LOGGER.info("Row inserted into db");
            SQLUtils.updateAndSetGeneratedId(preparedStatement, person::setPersonId);
            newPersonId = person.getPersonId();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionPool.releaseConnection(dbConnection);
        }
        return newPersonId;
    }

    @Override
    public Person getById(int personId) {
        Connection dbConnection = connectionPool.getConnection();
        Person person = null;
        try (
             PreparedStatement preparedStatement = dbConnection.prepareStatement(GET_BY_ID_QUERY)
        ) {
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
            connectionPool.releaseConnection(dbConnection);
        }
        return person;
    }

    @Override
    public void update(Person person) {
        Connection dbConnection = connectionPool.getConnection();
        try (
             PreparedStatement preparedStatement = dbConnection.prepareStatement(UPDATE_QUERY)
        ) {
            preparedStatement.setString(1, person.getFirstName());
            preparedStatement.setString(2, person.getLastName());
            preparedStatement.setInt(3, person.getPersonId());
            LOGGER.info("Row updated in DB: Person ID - " + person.getPersonId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating person in the database", e);
        } finally {
            connectionPool.releaseConnection(dbConnection);
        }
    }

    @Override
    public void delete(int personId) {
        Connection dbConnection = connectionPool.getConnection();
        try (
             PreparedStatement preparedStatement = dbConnection.prepareStatement(DELETE_QUERY)
        ) {
            preparedStatement.setInt(1, personId);
            LOGGER.info("Row deleted from DB: Person ID - " + personId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting person from the database", e);
        } finally {
            connectionPool.releaseConnection(dbConnection);
        }
    }
}
