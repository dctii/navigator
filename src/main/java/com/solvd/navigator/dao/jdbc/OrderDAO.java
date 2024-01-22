package com.solvd.navigator.dao.jdbc;

import com.solvd.navigator.bin.Order;
import com.solvd.navigator.util.DBConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderDAO {
    private static final Logger LOGGER = (Logger) LogManager.getLogger(OrderDAO.class);
    private static final String OrderSQL = "INSERT INTO navigator.order(order_int,order_number,order_status,order_date,delivery_date,storage_id,order_recipient,driver_id) VALUES (?,?,?,?,?,?,?,?)";
    private Connection dbConnection;

    public void addOrder(Order order) {
        try (Connection dbConnection = DBConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(OrderSQL)) {
            preparedStatement.setInt(1, order.getOrderId());
            preparedStatement.setString(2, order.getOrderNumber());
            preparedStatement.setString(3, order.getOrderStatus());
            preparedStatement.setTimestamp(4, order.getOrderDate());
            preparedStatement.setTimestamp(5, order.getDeliveryDate());
            preparedStatement.setInt(6, order.getStorageId());
            preparedStatement.setInt(7, order.getOrderRecipientId());
            preparedStatement.setInt(8, order.getDriverId());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnectionPool.getInstance().releaseConnection(dbConnection);
        }
    }
}
