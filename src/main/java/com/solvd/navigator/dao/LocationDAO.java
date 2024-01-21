package com.solvd.navigator.dao;

import com.solvd.navigator.bin.Location;
import com.solvd.navigator.util.DBConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationDAO implements AbstractDAO<Location> {
    private static final Logger LOGGER = (Logger) LogManager.getLogger(DriverDAO.class);
    private static final String CREATE_LOCATION = "INSERT INTO locations (location_id, coordinate_x, coordinate_y)" + "VALUES (?,?,?)";
    private static final String GET_BY_ID_QUERY = "SELECT * FROM locations WHERE location_id = ?";
    private static final String UPDATE_QUERY = "UPDATE locations SET coordinate_x=?, coordinate_y=? WHERE location_id=?";
    private static final String DELETE_QUERY = "DELETE FROM locations WHERE location_id=?";

    private Connection dbConnection;


    @Override
    public int create(Location location) {
        try (Connection dbConnection = DBConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(CREATE_LOCATION)) {
            preparedStatement.setInt(1, location.getLocationId());
            preparedStatement.setFloat(2, location.getCoordinateX());
            preparedStatement.setFloat(3, location.getCoordinateY());
            LOGGER.info("Row inserted into DB");
            return preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException("Error adding location to the database",e);
        } finally {
            DBConnectionPool.getInstance().releaseConnection(dbConnection);
        }
    }

    @Override
    public Location getById(int locationId) {
        Location location = null;
        try (Connection dbConnection = DBConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(GET_BY_ID_QUERY)) {
            preparedStatement.setInt(1, locationId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // If a record is found, create a Location object
                    location = new Location(
                            resultSet.getInt("location_id"),
                            resultSet.getFloat("coordinate_x"),
                            resultSet.getFloat("coordinate_y")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting location from the database", e);
        } finally {
            DBConnectionPool.getInstance().releaseConnection(dbConnection);
        }
        return location;
    }

    @Override
    public void update(Location location) {
        try (Connection dbConnection = DBConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(UPDATE_QUERY)) {
            preparedStatement.setFloat(1, location.getCoordinateX());
            preparedStatement.setFloat(2, location.getCoordinateY());
            preparedStatement.setInt(3, location.getLocationId());
            LOGGER.info("Row updated in DB: Location ID - " + location.getLocationId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating location in the database", e);
        } finally {
            DBConnectionPool.getInstance().releaseConnection(dbConnection);
        }
    }

    @Override
    public void delete(int locationId) {
        try (Connection dbConnection = DBConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(DELETE_QUERY)) {
            preparedStatement.setInt(1, locationId);
            LOGGER.info("Row deleted from DB: Location ID - " + locationId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting location from the database", e);
        } finally {
            DBConnectionPool.getInstance().releaseConnection(dbConnection);
        }
    }
}
