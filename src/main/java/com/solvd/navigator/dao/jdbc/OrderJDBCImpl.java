package com.solvd.navigator.dao.jdbc;

import com.solvd.navigator.bin.Order;
import com.solvd.navigator.dao.OrderDAO;
import com.solvd.navigator.util.DBConnectionPool;
import com.solvd.navigator.util.SQLUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderJDBCImpl implements OrderDAO {
    private static final Logger LOGGER = LogManager.getLogger(OrderJDBCImpl.class);
    private static final String CREATE_ORDER_SQL = "INSERT INTO orders (order_number,order_status,order_date,delivery_date,storage_id,order_recipient_id,driver_id) VALUES (?,?,?,?,?,?,?)";
    private static final String SELECT_ORDER_SQL = "SELECT * FROM orders WHERE order_id = ?";
    private static final String AWAITING_DELIVERY_QUERY = "SELECT * FROM orders WHERE storage_id = ? AND order_status = 'Awaiting Delivery'";
    private static final String LIMIT_AWAITING_DELIVERY_QUERY = "SELECT * FROM orders WHERE storage_id = ? AND order_status = 'Awaiting Delivery' LIMIT ?";
    private static final String UPDATE_ORDER_SQL = "UPDATE orders SET order_number = ?, order_status = ?, order_date = ?, delivery_date = ?, storage_id = ?, order_recipient_id = ?, driver_id = ? WHERE order_id = ?";
    private static final String DELETE_ORDER_SQL = "DELETE FROM orders WHERE order_id = ?";
    DBConnectionPool connectionPool = DBConnectionPool.getInstance();

    public int create(Order order) {
        Connection dbConnection = connectionPool.getConnection();

        int newOrderId = 0;
        try (
                PreparedStatement preparedStatement = dbConnection.prepareStatement(
                        CREATE_ORDER_SQL,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {

            SQLUtils.setStringOrNull(preparedStatement,1, order.getOrderNumber());
            SQLUtils.setStringOrNull(preparedStatement,2, order.getOrderStatus());
            SQLUtils.setTimestampOrNull(preparedStatement,3, order.getOrderDate());
            SQLUtils.setTimestampOrNull(preparedStatement,4, order.getDeliveryDate());
            SQLUtils.setIntOrNull(preparedStatement,5, order.getStorageId());
            SQLUtils.setIntOrNull(preparedStatement,6, order.getOrderRecipientId());
            SQLUtils.setIntOrNull(preparedStatement,7, order.getDriverId());

            SQLUtils.updateAndSetGeneratedId(preparedStatement, order::setOrderId);
            newOrderId = order.getOrderId();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionPool.releaseConnection(dbConnection);
        }
        return newOrderId;
    }


    @Override
    public Order getById(int orderId) {
        Connection dbConnection = connectionPool.getConnection();

        Order order = null;
        try (
                PreparedStatement preparedStatement = dbConnection.prepareStatement(SELECT_ORDER_SQL)) {
            preparedStatement.setInt(1, orderId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    order = new Order(
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
        } finally {
            connectionPool.releaseConnection(dbConnection);
        }
        return order;
    }

    public List<Order> getAllAwaitingOrdersByStorageId(int storageId) {
        Connection dbConnection = connectionPool.getConnection();
        List<Order> awaitingOrdersOfTargetStorage = new ArrayList<>();

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(AWAITING_DELIVERY_QUERY)) {
            preparedStatement.setInt(1, storageId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    // If an order is found, create a Storage object and add it to the list
                    Order awaitingOrder = new Order(
                            resultSet.getInt("order_id"),
                            resultSet.getString("order_number"),
                            resultSet.getString("order_status"),
                            resultSet.getTimestamp("order_date"),
                            resultSet.getTimestamp("delivery_date"),
                            resultSet.getInt("storage_id"),
                            resultSet.getInt("order_recipient_id"),
                            resultSet.getInt("driver_id")
                    );
                    awaitingOrdersOfTargetStorage.add(awaitingOrder);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting storages with awaiting orders from the database", e);
        } finally {
            connectionPool.releaseConnection(dbConnection);
        }

        return awaitingOrdersOfTargetStorage;
    }


    public List<Order> getLimitedAwaitingOrdersByStorageId(int storageId, int orderLimit) {
        Connection dbConnection = connectionPool.getConnection();
        List<Order> awaitingOrdersOfTargetStorage = new ArrayList<>();

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(LIMIT_AWAITING_DELIVERY_QUERY)) {
            preparedStatement.setInt(1, storageId);
            preparedStatement.setInt(2, orderLimit);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    // If an order is found, create a Storage object and add it to the list
                    Order awaitingOrder = new Order(
                            resultSet.getInt("order_id"),
                            resultSet.getString("order_number"),
                            resultSet.getString("order_status"),
                            resultSet.getTimestamp("order_date"),
                            resultSet.getTimestamp("delivery_date"),
                            resultSet.getInt("storage_id"),
                            resultSet.getInt("order_recipient_id"),
                            resultSet.getInt("driver_id")
                    );
                    awaitingOrdersOfTargetStorage.add(awaitingOrder);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting storages with awaiting orders from the database", e);
        } finally {
            connectionPool.releaseConnection(dbConnection);
        }

        return awaitingOrdersOfTargetStorage;
    }


    @Override
    public void update(Order order) {
        Connection dbConnection = connectionPool.getConnection();

        try (
                PreparedStatement preparedStatement = dbConnection.prepareStatement(UPDATE_ORDER_SQL)
        ) {
            SQLUtils.setStringOrNull(preparedStatement,1, order.getOrderNumber());
            SQLUtils.setStringOrNull(preparedStatement,2, order.getOrderStatus());
            SQLUtils.setTimestampOrNull(preparedStatement,3, order.getOrderDate());
            SQLUtils.setTimestampOrNull(preparedStatement,4, order.getDeliveryDate());
            SQLUtils.setIntOrNull(preparedStatement,5, order.getStorageId());
            SQLUtils.setIntOrNull(preparedStatement,6,order.getOrderRecipientId());
            SQLUtils.setIntOrNull(preparedStatement,7,order.getDriverId() == 0 ? null : order.getDriverId());
            SQLUtils.setIntOrNull(preparedStatement,8, order.getOrderId());
            preparedStatement.executeUpdate();
            LOGGER.info("ROW UPDATED IN DB");
        } catch (SQLException e) {
            throw new RuntimeException("Error updating order",e);
        } finally {
            connectionPool.releaseConnection(dbConnection);
        }

    }

    @Override
    public void delete(int orderId) {
        Connection dbConnection = connectionPool.getConnection();

        try (
                PreparedStatement preparedStatement = dbConnection.prepareStatement(DELETE_ORDER_SQL)
        ) {
            preparedStatement.setInt(1, orderId);
            preparedStatement.executeQuery();
            LOGGER.info("ROW DELETED FROM DB");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionPool.releaseConnection(dbConnection);
        }

    }
}
