package com.solvd.navigator.dao.jdbc;

import com.solvd.navigator.bin.Driver;
import com.solvd.navigator.dao.DriverDAO;
import com.solvd.navigator.util.DBConnectionPool;
import com.solvd.navigator.util.SQLUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.sql.*;


public class DriverJDBCImpl implements DriverDAO {
    private static final Logger LOGGER = (Logger) LogManager.getLogger(DriverJDBCImpl.class);
    private static final String CREATE_DRIVER = "INSERT INTO drivers (employee_id,vehicle_id)" + "VALUES (?,?)";
    private static final String GET_BY_ID  = "SELECT * From drivers Where driver_id = ?";
    private static final String UPDATE_QUERY  = "UPDATE drivers SET employee_id=?, vehicle_id=? WHERE driver_id=?";
    private static final String DELETE_QUERY  = "DELETE FROM drivers WHERE driver_id=?";
    private final DBConnectionPool connectionPool = DBConnectionPool.getInstance();


    @Override
    public int create(Driver driver) {
        Connection dbConnection = connectionPool.getConnection();
        int newDriverId = 0;
        try (
                PreparedStatement preparedStatement = dbConnection.prepareStatement(
                        CREATE_DRIVER,
                        Statement.RETURN_GENERATED_KEYS

                )
        ) {
            preparedStatement.setInt(1, driver.getEmployeeId());
            preparedStatement.setInt(2, driver.getVehicleId());
            LOGGER.info("Row inserted into DB");

            // Create a new Builder and set the generated driverId
            Driver.Builder builder = new Driver.Builder();
            SQLUtils.updateAndSetGeneratedId(preparedStatement, builder::setDriverId);

            // Build the Driver instance
            Driver newDriver = builder
                    .setEmployeeId(driver.getEmployeeId())
                    .setVehicleId(driver.getVehicleId())
                    .build();

            // Retrieve the updated driverId from the newDriver
            newDriverId = newDriver.getDriverId();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding driver to the database", e);
        } finally {
            DBConnectionPool.getInstance().releaseConnection(dbConnection);
        }

        return newDriverId;
    }

    @Override
    public Driver getById(int driverId) {
        Connection dbConnection = connectionPool.getConnection();
        Driver driver = null;
        try (
             PreparedStatement preparedStatement = dbConnection.prepareStatement(GET_BY_ID)
        ) {
            preparedStatement.setInt(1, driverId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // If a record is found, create a Driver object
                    Driver.Builder builder = new Driver.Builder()
                            .setDriverId(resultSet.getInt("driver_id"))
                            .setEmployeeId(resultSet.getInt("employee_id"))
                            .setVehicleId(resultSet.getInt("vehicle_id"));

                    driver = builder.build();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting driver from the database", e);
        } finally {
            DBConnectionPool.getInstance().releaseConnection(dbConnection);
        }
        return driver;
    }

    @Override
    public void update(Driver driver) {
        Connection dbConnection = connectionPool.getConnection();
        try (
             PreparedStatement preparedStatement = dbConnection.prepareStatement(UPDATE_QUERY)
        ) {
            preparedStatement.setInt(1, driver.getEmployeeId());
            preparedStatement.setInt(2, driver.getVehicleId());
            preparedStatement.setInt(3, driver.getDriverId());
            LOGGER.info("Row updated in DB: Driver ID - " + driver.getDriverId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating driver in the database", e);
        } finally {
            DBConnectionPool.getInstance().releaseConnection(dbConnection);
        }
    }

    @Override
    public void delete(int driverId) {
        Connection dbConnection = connectionPool.getConnection();
        try (
             PreparedStatement preparedStatement = dbConnection.prepareStatement(DELETE_QUERY)
        ) {
            preparedStatement.setInt(1, driverId);
            LOGGER.info("Row deleted from DB: Driver ID - " + driverId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting driver from the database", e);
        } finally {
            DBConnectionPool.getInstance().releaseConnection(dbConnection);
        }
    }
}
