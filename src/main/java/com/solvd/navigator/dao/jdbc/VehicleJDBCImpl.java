package com.solvd.navigator.dao.jdbc;

import com.solvd.navigator.bin.Vehicle;
import com.solvd.navigator.dao.VehicleDAO;
import com.solvd.navigator.util.DBConnectionPool;
import com.solvd.navigator.util.SQLUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class VehicleJDBCImpl implements VehicleDAO {

    private static final Logger LOGGER = LogManager.getLogger(VehicleJDBCImpl.class);

    private static final String CREATE_VEHICLE_SQL = "INSERT INTO vehicles(year,make,model,trim_level,license_plate_number) VALUES (?,?,?,?,?)";
    private static final String SELECT_VEHICLE_SQL = "SELECT * FROM vehicles WHERE vehicle_id = ?";
    private static final String UPDATE_VEHICLE_SQL = "UPDATE vehicles SET year = ?, make = ?, model = ?, trim_level = ?, license_plate_number = ? WHERE vehicle_id = ?";
    private static final String DELETE_VEHICLE_SQL = "DELETE FROM vehicles WHERE vehicle_id = ?";

    // Put the connection pool up here. All the queries below will use it, so we are able to instantiate it once for this class.
    private final DBConnectionPool connectionPool = DBConnectionPool.getInstance();

    @Override
    public int create(Vehicle vehicle) {
        // Put the connection outside this 'try with resources'
        Connection dbConnection = connectionPool.getConnection();

        // Set an int variable to 0. If it doesn't return a vehicleId, then it means that it was unsuccessful for some reason in making the id
        int newVehicleId = 0;
        try (
                PreparedStatement preparedStatement = dbConnection.prepareStatement(
                        CREATE_VEHICLE_SQL,
                        // the primary key 'vehicle_id` is auto-incremented, so it will be automatically generated
                        // so you need to make sure to put this 'Statement.RETURN_GENERATED_KEYS' as the 2nd argument of prepareStatement()
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {
            /*
             You'll notice here that we removed 'vehicle.getVehicleId()',
             this is because we do not set the PRIMARY KEY 'vehicle_id'
             because the database will generate it automatically.

             So, you need to make sure and slightly modify the SQL query you have for this...

            Change it from this:

                private static final String CreateVehicleSQL =
                     "INSERT INTO vehicles(vehicle_id,year,make,model,trim_level,license_plate_number) VALUES (?,?,?,?,?,?)";

            to this--note that I removed 'vehicle_id' and then one of the placeholder '?' from VALUES(...) in the String
            there should be 5 '?' in VALUES(...):

                private static final String CreateVehicleSQL =
                     "INSERT INTO navigator.vehicle(year,make,model,trim_level,license_plate_number) VALUES (?,?,?,?,?)";

             */

            SQLUtils.setIntOrNull(preparedStatement,1, vehicle.getYear());
            SQLUtils.setStringOrNull(preparedStatement,2, vehicle.getMake());
            SQLUtils.setStringOrNull(preparedStatement,3, vehicle.getModel());
            SQLUtils.setStringOrNull(preparedStatement,4, vehicle.getTrimLevel());
            SQLUtils.setStringOrNull(preparedStatement,5, vehicle.getLicensePlateNumber());

            // Use this custom helper tool that was put in
            SQLUtils.updateAndSetGeneratedId(
                    // we pass the prepared statement so that we can use it to do
                    // preparedStatement.executeQuery(), which executes the SQL query
                    preparedStatement,
                    // after the query has been completed, we will get the auto-incremented id and then we set it to this 'vehicle'
                    vehicle::setVehicleId
            );

            newVehicleId = vehicle.getVehicleId();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionPool.releaseConnection(dbConnection);
        }
        return newVehicleId;
    }

    @Override
    public Vehicle getById(int vehicleId) {
        Connection dbConnection = connectionPool.getConnection();

        Vehicle vehicle = null;
        try (
                PreparedStatement preparedStatement = dbConnection.prepareStatement(SELECT_VEHICLE_SQL)
        ) {
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
        } finally {
            connectionPool.releaseConnection(dbConnection);
        }
        return vehicle;
    }

    @Override
    public void update(Vehicle vehicle) {
        Connection dbConnection = connectionPool.getConnection();

        try (
                PreparedStatement preparedStatement = dbConnection.prepareStatement(UPDATE_VEHICLE_SQL)
        ) {
            preparedStatement.setInt(1, vehicle.getYear());
            preparedStatement.setString(2, vehicle.getMake());
            preparedStatement.setString(3, vehicle.getModel());
            preparedStatement.setString(4, vehicle.getTrimLevel());
            preparedStatement.setString(5, vehicle.getLicensePlateNumber());
            preparedStatement.setInt(6, vehicle.getVehicleId());
            preparedStatement.executeUpdate();
            LOGGER.info("ROW UPDATED IN DB");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionPool.releaseConnection(dbConnection);
        }
    }

    @Override
    public void delete(int vehicleId) {
        Connection dbConnection = connectionPool.getConnection();

        try (
                PreparedStatement preparedStatement = dbConnection.prepareStatement(DELETE_VEHICLE_SQL)
        ) {
            preparedStatement.setInt(1, vehicleId);
            preparedStatement.executeUpdate();
            LOGGER.info("ROW DELETED FROM DB");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionPool.releaseConnection(dbConnection);
        }
    }

}
