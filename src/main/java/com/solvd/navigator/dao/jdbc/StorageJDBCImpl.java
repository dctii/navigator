package com.solvd.navigator.dao.jdbc;

import com.solvd.navigator.bin.Storage;
import com.solvd.navigator.dao.StorageDAO;
import com.solvd.navigator.util.DBConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StorageJDBCImpl implements StorageDAO {

    private static final Logger LOGGER = (Logger) LogManager.getLogger(StorageJDBCImpl.class);
    private static final String CreateStorageSQL = "INERT INTO navigator.storages(storage_id,name,location_id) VALUES (?,?,?)";
    private static final String SelectStorageSQL = "SELECT * FROM storages WHERE storage_id = ?";
    private static final String UpdateStorageSQL = "UPDATE storages SET name = ? WHERE storage_id = ?";
    private static final String DeleteStorageSQL = "DELETE FROM storages WHERE storage_id = ?";
    private Connection dbConnection;

    public int create(Storage storage) {
        try (Connection dbConnection = DBConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(CreateStorageSQL)) {
            preparedStatement.setInt(1, storage.getStorageId());
            preparedStatement.setString(2, storage.getName());
            preparedStatement.setInt(3, storage.getLocationId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnectionPool.getInstance().releaseConnection(dbConnection);
        }
       return storage.getStorageId();
    }

    @Override
    public Storage getById(int storageId) {
        Storage storage = null;
        try (Connection dbConnection = DBConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(SelectStorageSQL)) {
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

    @Override
    public void update(Storage storage) {
        try (Connection dbConnection = DBConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(UpdateStorageSQL)) {
            preparedStatement.setString(1, storage.getName());
            preparedStatement.setInt(2, storage.getStorageId());
            preparedStatement.executeUpdate();
            LOGGER.info("ROW updated in DB");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnectionPool.getInstance().releaseConnection(dbConnection);
        }
    }

    @Override
    public void delete(int storageId) {
        try (Connection dbConnection = DBConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(DeleteStorageSQL)) {
            preparedStatement.setInt(1, storageId);
            preparedStatement.executeUpdate();
            LOGGER.info("ROW deleted from DB");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnectionPool.getInstance().releaseConnection(dbConnection);
        }
    }
}
