package com.solvd.navigator.dao.jdbc;

import com.solvd.navigator.bin.Employee;
import com.solvd.navigator.dao.EmployeeDAO;
import com.solvd.navigator.util.DBConnectionPool;
import com.solvd.navigator.util.SQLUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmployeeJDBCImpl implements EmployeeDAO {
    private static final Logger LOGGER = LogManager.getLogger(EmployeeJDBCImpl.class);

    private static final String CREATE_EMPLOYEE = "INSERT INTO employees (role, person_id)" + "VALUES (?,?)";
    private static final String GET_BY_ID_QUERY = "SELECT * FROM employees WHERE employee_id = ?";
    private static final String GET_ALL_QUERY = "SELECT * FROM employees";
    private static final String UPDATE_QUERY = "UPDATE employees SET role=?, person_id=? WHERE employee_id=?";
    private static final String DELETE_QUERY = "DELETE FROM employees WHERE employee_id=?";
    private final DBConnectionPool connectionPool = DBConnectionPool.getInstance();



    @Override
    public int create(Employee employee) {
        Connection dbConnection = connectionPool.getConnection();
        int newEmployeeId = 0;
        try (
                PreparedStatement preparedStatement = dbConnection.prepareStatement(
                        CREATE_EMPLOYEE,
                        Statement.RETURN_GENERATED_KEYS)
             ) {
            SQLUtils.setStringOrNull(preparedStatement,1, employee.getRole());
            SQLUtils.setIntOrNull(preparedStatement,2, employee.getPersonId());
            LOGGER.info("Row inserted into DB");
            SQLUtils.updateAndSetGeneratedId(preparedStatement, employee::setEmployeeId);
            newEmployeeId= employee.getEmployeeId();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding Employee to database.", e);
        } finally {
            connectionPool.releaseConnection(dbConnection);
        }
        return newEmployeeId;
    }

    @Override
    public Employee getById(int employeeId) {
        Connection dbConnection = connectionPool.getConnection();
        Employee employee = null;
        try (
             PreparedStatement preparedStatement = dbConnection.prepareStatement(GET_BY_ID_QUERY)
        ) {
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
            connectionPool.releaseConnection(dbConnection);
        }
        return employee;
    }

    public List<Employee> getAll() {
        Connection dbConnection = connectionPool.getConnection();
        List<Employee> allEmployees = new ArrayList<>();
        try (
                PreparedStatement preparedStatement = dbConnection.prepareStatement(GET_ALL_QUERY)
        ) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    // If a record is found, create an Employee object
                    Employee employee = new Employee(
                            resultSet.getInt("employee_id"),
                            resultSet.getString("role"),
                            resultSet.getInt("person_id")
                    );
                    allEmployees.add(employee);

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting employee from the database", e);
        } finally {
            connectionPool.releaseConnection(dbConnection);
        }
        return allEmployees;
    }

    @Override
    public void update(Employee employee) {
        Connection dbConnection = connectionPool.getConnection();
        try (
             PreparedStatement preparedStatement = dbConnection.prepareStatement(UPDATE_QUERY)
        ) {
            preparedStatement.setString(1, employee.getRole());
            preparedStatement.setInt(2, employee.getPersonId());
            preparedStatement.setInt(3, employee.getEmployeeId());
            LOGGER.info("Row updated in DB: Employee ID - " + employee.getEmployeeId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating employee in the database", e);
        } finally {
            connectionPool.releaseConnection(dbConnection);
        }
    }

    @Override
    public void delete(int employeeId) {
        Connection dbConnection = connectionPool.getConnection();
        try (

             PreparedStatement preparedStatement = dbConnection.prepareStatement(DELETE_QUERY)
        ) {
            preparedStatement.setInt(1, employeeId);
            LOGGER.info("Row deleted from DB: Employee ID - " + employeeId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting employee from the database", e);
        } finally {
            connectionPool.releaseConnection(dbConnection);
        }
    }
}
