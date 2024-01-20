package com.solvd.navigator.dao;

import com.solvd.navigator.bin.Vehicle;
import com.solvd.navigator.util.DBConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VehicleDAO {

    public static final Logger LOGGER = (Logger) LogManager.getLogger(VehicleDAO.class);
    private static final String VehicleSQL = "INSERT INTO navigator.vehicle(vehicle_id,year,make,model,trim_level,license_plate_number) VALUES (?,?,?,?,?,?)";
    private Connection dbConnection;
    public void addVehicle(Vehicle vehicle) {
        try (Connection dbConnection = DBConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(VehicleSQL)) {
            preparedStatement.setInt(1, vehicle.getVehicleId());
            preparedStatement.setInt(2, vehicle.getYear());
            preparedStatement.setString(3, vehicle.getMake());
            preparedStatement.setString(4, vehicle.getModel());
            preparedStatement.setString(5, vehicle.getTrimLevel());
            preparedStatement.setString(6, vehicle.getLicensePlateNumber());
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        finally {
            DBConnectionPool.getInstance().releaseConnection(dbConnection);
        }
    }
}
