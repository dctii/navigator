package com.solvd.navigator.dao.jdbc;

import com.solvd.navigator.bin.OrderRecipient;
import com.solvd.navigator.dao.OrderRecipientDAO;
import com.solvd.navigator.util.DBConnectionPool;
import org.apache.logging.log4j.LogManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class OrderRecipientJDBCImpl implements OrderRecipientDAO {

    private static final Logger LOGGER = (Logger) LogManager.getLogger(OrderRecipientJDBCImpl.class);
    private static final String CreateRecipientSQL = "INSERT INTO navigator.order_recipients(order_recipient_id,person_id,location_id) VALUES (?,?,?)";
    private static final String SelectRecipientSQL = "SELECT * FROM order_recipients WHERE order_recipient_id = ?";
    private static final String UpdateRecipientSQL = "UPDATE order_recipients SET location_id = ? WHERE order_recipient_id = ?";
    private static final String DeleteRecipientSQL = "DELETE FROM order_recipients WHERE order_recipient_id = ?";
    private Connection dbConnection;

    @Override
    public int create(OrderRecipient orderRecipient) {
        try (Connection dbConnection = DBConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(CreateRecipientSQL)) {
            preparedStatement.setInt(1, orderRecipient.getOrderRecipientId());
            preparedStatement.setInt(2, orderRecipient.getPersonId());
            preparedStatement.setInt(3, orderRecipient.getLocationId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnectionPool.getInstance().releaseConnection(dbConnection);
        }
        return orderRecipient.getOrderRecipientId();
    }

    @Override
    public OrderRecipient getById(int orderRecipientId) {
        OrderRecipient orderRecipient = null;
        try (Connection dbConnection = DBConnectionPool.getInstance().getConnection();
            PreparedStatement preparedStatement = dbConnection.prepareStatement(SelectRecipientSQL)){
                preparedStatement.setInt(1, orderRecipientId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        orderRecipient = new OrderRecipient(
                                resultSet.getInt("order_recipient)id"),
                                resultSet.getInt("person_id"),
                                resultSet.getInt("location_id")
                        );
                    }
                }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void update(OrderRecipient orderRecipient) {
        try (Connection dbConnection = DBConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(UpdateRecipientSQL)) {
            preparedStatement.setInt(1, orderRecipient.getLocationId());
            preparedStatement.setInt(2, orderRecipient.getOrderRecipientId());
            preparedStatement.executeUpdate();
            LOGGER.info("ROW updated in DB");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnectionPool.getInstance().releaseConnection(dbConnection);
        }
    }

    @Override
    public void delete(int orderRecipientId) {
        try (Connection dbConnection = DBConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(DeleteRecipientSQL)) {
            preparedStatement.setInt(1, orderRecipientId);
            preparedStatement.executeUpdate();
            LOGGER.info("ROW deleted from DB");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnectionPool.getInstance().releaseConnection(dbConnection);
        }
    }
}