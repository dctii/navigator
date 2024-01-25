package com.solvd.navigator.dao.jdbc;

import com.solvd.navigator.bin.OrderRecipient;
import com.solvd.navigator.dao.OrderRecipientDAO;
import com.solvd.navigator.util.DBConnectionPool;
import com.solvd.navigator.util.SQLUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class OrderRecipientJDBCImpl implements OrderRecipientDAO {

    private static final Logger LOGGER = LogManager.getLogger(OrderRecipientJDBCImpl.class);
    private static final String CREATE_RECIPIENT_SQL= "INSERT INTO order_recipients(person_id,location_id) VALUES (?,?)";
    private static final String SELECT_RECIPIENT_SQL = "SELECT * FROM order_recipients WHERE order_recipient_id = ?";
    private static final String GET_ALL_QUERY = "SELECT * FROM order_recipients";
    private static final String UPDATE_RECIPIENT_SQL = "UPDATE order_recipients SET person_id = ?, location_id = ? WHERE order_recipient_id = ?";
    private static final String DELETE_RECIPIENT_SQL = "DELETE FROM order_recipients WHERE order_recipient_id = ?";

    DBConnectionPool connectionPool = DBConnectionPool.getInstance();

    @Override
    public int create(OrderRecipient orderRecipient) {
        Connection dbConnection = connectionPool.getConnection();

        int newOrderRecipientId = 0;
        try (
                PreparedStatement preparedStatement = dbConnection.prepareStatement(
                        CREATE_RECIPIENT_SQL,
                        PreparedStatement.RETURN_GENERATED_KEYS
                )
        ) {
            SQLUtils.setIntOrNull(preparedStatement,1, orderRecipient.getPersonId());
            SQLUtils.setIntOrNull(preparedStatement,2, orderRecipient.getLocationId());

            SQLUtils.updateAndSetGeneratedId(preparedStatement, orderRecipient::setOrderRecipientId);

            newOrderRecipientId = orderRecipient.getOrderRecipientId();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionPool.releaseConnection(dbConnection);
        }
        return newOrderRecipientId;
    }

    @Override
    public OrderRecipient getById(int orderRecipientId) {
        Connection dbConnection = connectionPool.getConnection();

        OrderRecipient orderRecipient = null;
        try (
                PreparedStatement preparedStatement = dbConnection.prepareStatement(SELECT_RECIPIENT_SQL)
        ) {
            preparedStatement.setInt(1, orderRecipientId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    orderRecipient = new OrderRecipient(
                            resultSet.getInt("order_recipient_id"),
                            resultSet.getInt("person_id"),
                            resultSet.getInt("location_id")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionPool.releaseConnection(dbConnection);
        }
        return orderRecipient;
    }

    public List<OrderRecipient> getAll() {
        Connection dbConnection = connectionPool.getConnection();
        List<OrderRecipient> allOrderRecipients = new ArrayList<>();
        try (
                PreparedStatement preparedStatement = dbConnection.prepareStatement(GET_ALL_QUERY)
                ) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    OrderRecipient orderRecipient = new OrderRecipient(
                            resultSet.getInt("order_recipient_id"),
                            resultSet.getInt("person_id"),
                            resultSet.getInt("location_id")
                    );
                    allOrderRecipients.add(orderRecipient);
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionPool.releaseConnection(dbConnection);
        }
        return allOrderRecipients;
    }

    @Override
    public void update(OrderRecipient orderRecipient) {
        Connection dbConnection = connectionPool.getConnection();

        try (
                PreparedStatement preparedStatement = dbConnection.prepareStatement(UPDATE_RECIPIENT_SQL)
        ) {
            preparedStatement.setInt(1, orderRecipient.getPersonId());
            preparedStatement.setInt(2, orderRecipient.getLocationId());
            preparedStatement.setInt(3, orderRecipient.getOrderRecipientId());
            preparedStatement.executeUpdate();
            LOGGER.info("ROW updated in DB");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionPool.releaseConnection(dbConnection);
        }
    }

    @Override
    public void delete(int orderRecipientId) {
        Connection dbConnection = connectionPool.getConnection();

        try (
                PreparedStatement preparedStatement = dbConnection.prepareStatement(DELETE_RECIPIENT_SQL)
        ) {
            preparedStatement.setInt(1, orderRecipientId);
            preparedStatement.executeUpdate();
            LOGGER.info("ROW deleted from DB");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionPool.releaseConnection(dbConnection);
        }
    }
}
