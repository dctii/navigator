package com.solvd.navigator.dao;

import com.solvd.navigator.bin.Employee;
import com.solvd.navigator.util.DBConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeDAO implements AbstractDAO<Employee> {
    private static final Logger LOGGER = (Logger) LogManager.getLogger(EmployeeDAO.class);

    private static final String CREATE_EMPLOYEE = "INSERT INTO employees (employee_id, role, person_id)" + "VALUES (?,?,?)";
    private static final String GET_BY_ID_QUERY = "SELECT * FROM employees WHERE employee_id = ?";
    private static final String UPDATE_QUERY = "UPDATE employees SET role=?, person_id=? WHERE employee_id=?";
    private static final String DELETE_QUERY = "DELETE FROM employees WHERE employee_id=?";

    private Connection dbConnection;


    @Override
    public int create(Employee employee) {
        try (Connection dbConnection = DBConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(CREATE_EMPLOYEE)) {
            preparedStatement.setInt(1, employee.getEmployeeID());
            preparedStatement.setString(2, employee.getRole());
            preparedStatement.setInt(3, employee.getPersonId());
            LOGGER.info("Row inserted into DB");
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding Employee to database.", e);
        } finally {
            DBConnectionPool.getInstance().releaseConnection(dbConnection);
        }
    }

    @Override
    public Employee getById(int employeeId) {
        Employee employee = null;
        try (Connection dbConnection = DBConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(GET_BY_ID_QUERY)) {
            preparedStatement.setInt(1, employeeId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // If a record is found, create an Employee object
                    employee = new Employee(
                            resultSet.getInt("employee_id"),
                            resultSet.getString("role"),
                            resultSet.getInt("person_id")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting employee from the database", e);
        } finally {
            DBConnectionPool.getInstance().releaseConnection(dbConnection);
        }
        return employee;
    }

    @Override
    public void update(Employee employee) {
        try (Connection dbConnection = DBConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(UPDATE_QUERY)) {
            preparedStatement.setString(1, employee.getRole());
            preparedStatement.setInt(2, employee.getPersonId());
            preparedStatement.setInt(3, employee.getEmployeeID());
            LOGGER.info("Row updated in DB: Employee ID - " + employee.getEmployeeID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating employee in the database", e);
        } finally {
            DBConnectionPool.getInstance().releaseConnection(dbConnection);
        }
    }

    @Override
    public void delete(int employeeId) {
        try (Connection dbConnection = DBConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(DELETE_QUERY)) {
            preparedStatement.setInt(1, employeeId);
            LOGGER.info("Row deleted from DB: Employee ID - " + employeeId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting employee from the database", e);
        } finally {
            DBConnectionPool.getInstance().releaseConnection(dbConnection);
        }
    }
}
