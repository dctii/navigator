package com.solvd.navigator.dao.jdbc;

import com.solvd.navigator.bin.Storage;
import com.solvd.navigator.dao.StorageDAO;
import com.solvd.navigator.util.DBConnectionPool;
import com.solvd.navigator.util.SQLUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StorageJDBCImpl implements StorageDAO {

    private static final Logger LOGGER = LogManager.getLogger(StorageJDBCImpl.class);
    private static final String CREATE_STORAGE_SQL = "INSERT INTO storages(name,location_id) VALUES (?,?)";
    private static final String SELECT_STORAGE_SQL = "SELECT * FROM storages WHERE storage_id = ?";
    private static final String GET_ALL_QUERY = "SELECT * FROM storages";
    private static final String GET_STORAGE_BY_LOCATION_ID = "SELECT * FROM storages WHERE location_id = ?";
    private static final String UPDATE_STORAGE_SQL = "UPDATE storages SET storage_id = ?, name = ?, location_id = ? WHERE storage_id = ?";
    private static final String DELETE_STORAGE_SQL = "DELETE FROM storages WHERE storage_id = ?";
    private final DBConnectionPool connectionPool = DBConnectionPool.getInstance();

    public int create(Storage storage) {
        Connection dbConnection = connectionPool.getConnection();
        int newStorageId = 0;

        try (
             PreparedStatement preparedStatement = dbConnection.prepareStatement(
                     CREATE_STORAGE_SQL,
                     Statement.RETURN_GENERATED_KEYS)
        ) {
            SQLUtils.setStringOrNull(preparedStatement,1, storage.getName());
            SQLUtils.setIntOrNull(preparedStatement,2, storage.getLocationId());
            SQLUtils.updateAndSetGeneratedId(preparedStatement, storage::setStorageId);
            newStorageId = storage.getStorageId();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionPool.releaseConnection(dbConnection);
        }
        return newStorageId;
    }

    @Override
    public Storage getById(int storageId) {
        Connection dbConnection = connectionPool.getConnection();
        Storage storage = null;
        try (
             PreparedStatement preparedStatement = dbConnection.prepareStatement(SELECT_STORAGE_SQL)
        ) {
            preparedStatement.setInt(1, storageId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    storage = new Storage(
                            resultSet.getInt("storage_id"),
                            resultSet.getString("name"),
                            resultSet.getInt("location_id")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return storage;
    }
    public List<Storage> getAll() {
        Connection dbConnection = connectionPool.getConnection();
        List<Storage> allStorages = new ArrayList<>();
        try (
                PreparedStatement preparedStatement = dbConnection.prepareStatement(GET_ALL_QUERY)
                ) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    // If a record is found, create an Storage object
                    Storage storage = new Storage(
                            resultSet.getInt("storage_id"),
                            resultSet.getString("name"),
                            resultSet.getInt("location_id")
                    );
                    allStorages.add(storage);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error getting storage from the database",e);
        } finally {
            connectionPool.releaseConnection(dbConnection);
        }
        return  allStorages;

    }

    @Override
    public Storage getStorageByLocationId(int locationId) {
        Connection dbConnection = connectionPool.getConnection();
        Storage storageAtLocation = new Storage();
        try (
                PreparedStatement preparedStatement = dbConnection.prepareStatement(GET_STORAGE_BY_LOCATION_ID)
                ) {
            preparedStatement.setInt(1, locationId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Storage storage = new Storage(
                            resultSet.getInt("storage_id"),
                            resultSet.getString("name"),
                            resultSet.getInt("location_id")
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionPool.releaseConnection(dbConnection);
        }
        return storageAtLocation;
    }


    @Override
    public void update(Storage storage) {
        Connection dbConnection = connectionPool.getConnection();
        try (
             PreparedStatement preparedStatement = dbConnection.prepareStatement(UPDATE_STORAGE_SQL)
        ) {
            preparedStatement.setInt(1, storage.getStorageId());
            preparedStatement.setString(2, storage.getName());
            preparedStatement.setInt(3, storage.getLocationId());
            preparedStatement.setInt(4, storage.getStorageId());
            preparedStatement.executeUpdate();
            LOGGER.info("ROW updated in DB");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionPool.releaseConnection(dbConnection);
        }
    }

    @Override
    public void delete(int storageId) {
        Connection dbConnection = connectionPool.getConnection();
        try (
             PreparedStatement preparedStatement = dbConnection.prepareStatement(DELETE_STORAGE_SQL)
        ) {
            preparedStatement.setInt(1, storageId);
            preparedStatement.executeUpdate();
            LOGGER.info("ROW deleted from DB");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionPool.releaseConnection(dbConnection);
        }
    }
}
