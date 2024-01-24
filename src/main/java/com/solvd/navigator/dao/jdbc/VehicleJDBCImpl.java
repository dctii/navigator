package com.solvd.navigator.dao.jdbc;

import com.solvd.navigator.bin.Vehicle;
import com.solvd.navigator.dao.AbstractDAO;
import com.solvd.navigator.dao.VehicleDAO;
import com.solvd.navigator.util.DBConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VehicleJDBCImpl implements VehicleDAO {

    private static final Logger LOGGER = (Logger) LogManager.getLogger(VehicleJDBCImpl.class);
    private static final String CreateVehicleSQL = "INSERT INTO navigator.vehicle(vehicle_id,year,make,model,trim_level,license_plate_number) VALUES (?,?,?,?,?,?)";
    private static final String SelectVehicleSQL = "SELECT * FROM vehicles WHERE vehicle_id = ?";
    private static final String UpdateVehicleSQL = "UPDATE vehicles SET MODEL =? WHERE vehicle_id = ?";
    private static final String DeleteVehicleSQL = "DELETE FROM vehicles vehicle_id = ?";

    private Connection dbConnection;

    @Override
    public int create(Vehicle vehicle) {
        try (Connection dbConnection = DBConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(CreateVehicleSQL)) {
            preparedStatement.setInt(1, vehicle.getVehicleId());
            preparedStatement.setInt(2, vehicle.getYear());
            preparedStatement.setString(3, vehicle.getMake());
            preparedStatement.setString(4, vehicle.getModel());
            preparedStatement.setString(5, vehicle.getTrimLevel());
            preparedStatement.setString(6, vehicle.getLicensePlateNumber());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnectionPool.getInstance().releaseConnection(dbConnection);
        }
        return vehicle.getVehicleId();
    }

    @Override
    public Vehicle getById(int vehicleId) {
        Vehicle vehicle = null;
        try (Connection dbConnection = DBConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(SelectVehicleSQL)) {
            preparedStatement.setInt(1, vehicleId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    vehicle = new Vehicle(
                            resultSet.getInt("vehicle_id"),
                            resultSet.getInt("year"),
                            resultSet.getString("make"),
                            resultSet.getString("model"),
                            resultSet.getString("trim_level"),
                            resultSet.getString("licence_plate_number")
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vehicle;
    }

    @Override
    public void update(Vehicle vehicle) {
        try (Connection dbConnection = DBConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(UpdateVehicleSQL)) {
            preparedStatement.setString(1, vehicle.getModel());
            preparedStatement.setInt(2, vehicle.getVehicleId());
            preparedStatement.executeUpdate();
            LOGGER.info("ROW UPDATED IN DB");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnectionPool.getInstance().releaseConnection(dbConnection);
        }
    }

    @Override
    public void delete(int vehicleId) {
        try (Connection dbConnection = DBConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(DeleteVehicleSQL)) {
            preparedStatement.setInt(1, vehicleId);
            preparedStatement.executeUpdate();
            LOGGER.info("ROW DELETED FROM DB");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnectionPool.getInstance().releaseConnection(dbConnection);
        }
    }

}
