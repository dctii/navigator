package com.solvd.navigator.dao.jdbc;

import com.solvd.navigator.bin.Order;
import com.solvd.navigator.util.DBConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderJDBCImpl implements com.solvd.navigator.dao.OrderDAO {
    private static final Logger LOGGER = (Logger) LogManager.getLogger(OrderJDBCImpl.class);
    private static final String CreateOrderSQL = "INSERT INTO navigator.order(order_id,order_number,order_status,order_date,delivery_date,storage_id,order_recipient,driver_id) VALUES (?,?,?,?,?,?,?,?)";
    private static final String SelectOrderSQL = "SELECT * FROM orders WHERE order_id = ?";
    private static final String UpdateOrderSQL = "UPDATE orders SET order_number = ? WHERE order_id = ?";
    private static final String DeleteOrderSQL = "DELETE FROM orders WHERE order_id = ?";
    private Connection dbConnection;

    public int create(Order order) {
        try (Connection dbConnection = DBConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(CreateOrderSQL)) {
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
        return 0;
    }


    @Override
    public Order getById(int orderId) {
        Order order = null;
        try (Connection dbconnection = DBConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = dbconnection.prepareStatement(SelectOrderSQL)) {
           preparedStatement.setInt(1, orderId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Order Order = new Order(
                            resultSet.getInt("order_id"),
                            resultSet.getString("order_number"),
                            resultSet.getString("order_status"),
                            resultSet.getTimestamp("order_date"),
                            resultSet.getTimestamp("delivery_date"),
                            resultSet.getInt("storage_id"),
                            resultSet.getInt("order_recipient_id"),
                            resultSet.getInt("driver_id")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return order;
    }

    @Override
    public void update(Order order) {
        try (Connection dbConnection = DBConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(UpdateOrderSQL)) {
            preparedStatement.setInt(1, order.getOrderId());
            preparedStatement.setInt(2, order.getOrderId());
            preparedStatement.executeUpdate();
            LOGGER.info("ROW UPDATED IN DB");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnectionPool.getInstance().releaseConnection(dbConnection);
        }

    }

    @Override
    public void delete(int orderId) {
        try (Connection dbConnection = DBConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(DeleteOrderSQL)) {
            preparedStatement.setInt(1, orderId);
            preparedStatement.executeQuery();
            LOGGER.info("ROW DELETED FROM DB");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnectionPool.getInstance().releaseConnection(dbConnection);
        }

    }
}
