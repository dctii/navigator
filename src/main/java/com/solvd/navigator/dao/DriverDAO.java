package com.solvd.navigator.dao;

import com.solvd.navigator.bin.Driver;
import com.solvd.navigator.util.DBConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DriverDAO implements AbstractDAO<Driver> {
    private static final Logger LOGGER = (Logger) LogManager.getLogger(DriverDAO.class);
    private static final String CREATE_DRIVER = "INSERT INTO drivers (driver_id,employee_id,vehicle_id)" + "VALUES (?,?,?)";
    private static final String GET_BY_ID  = "SELECT * From drivers Where driver_id = ?";
    private static final String UPDATE_QUERY  = "UPDATE drivers SET employee_id=?, vehicle_id=? WHERE driver_id=?";
    private static final String DELETE_QUERY  = "DELETE FROM drivers WHERE driver_id=?";

    private Connection dbConnection;


    @Override
    public int create(Driver driver) {
        try (Connection dbConnection = DBConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(CREATE_DRIVER)) {
            preparedStatement.setInt(1, driver.getDriverId());
            preparedStatement.setInt(2, driver.getEmployeeId());
            preparedStatement.setInt(3, driver.getVehicleId());
            LOGGER.info("Row inserted into DB");
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding driver to the database", e);
        } finally {
            DBConnectionPool.getInstance().releaseConnection(dbConnection);
        }
    }

    @Override
    public Driver getById(int driverId) {
        Driver driver = null;
        try (Connection dbConnection = DBConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(GET_BY_ID)) {
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
        try (Connection dbConnection = DBConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(UPDATE_QUERY)) {
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
        try (Connection dbConnection = DBConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(DELETE_QUERY)) {
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
