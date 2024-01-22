package com.solvd.navigator.dao;

import com.solvd.navigator.bin.Storage;
import com.solvd.navigator.util.DBConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StrorageDAO {

    private static final Logger LOGGER = (Logger) LogManager.getLogger(StrorageDAO.class);
    private static final String StorageSQL = "INERT INTO navigator.storage(storage_id,name,location_id) VALUES (?,?,?)";
    private Connection dbConnection;

    public void addStorage(Storage storage) {
        try (Connection dbConnection = DBConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(StorageSQL)) {
            preparedStatement.setInt(1, storage.getStorageId());
            preparedStatement.setString(2, storage.getName());
            preparedStatement.setInt(3, storage.getLocationId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnectionPool.getInstance().releaseConnection(dbConnection);
        }

    }
}
