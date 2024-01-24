package com.solvd.navigator.dao.jdbc;

import com.solvd.navigator.bin.OrderRecipient;
import com.solvd.navigator.dao.OrderRecipientDAO;
import com.solvd.navigator.util.DBConnectionPool;
import com.solvd.navigator.util.SQLUtils;
import org.apache.logging.log4j.LogManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class OrderRecipientJDBCImpl implements OrderRecipientDAO {

    private static final Logger LOGGER = (Logger) LogManager.getLogger(OrderRecipientJDBCImpl.class);
    private static final String CreateRecipientSQL = "INSERT INTO order_recipients(person_id,location_id) VALUES (?,?)";
    private static final String SelectRecipientSQL = "SELECT * FROM order_recipients WHERE order_recipient_id = ?";
    private static final String UpdateRecipientSQL = "UPDATE order_recipients SET person_id = ?, location_id = ? WHERE order_recipient_id = ?";
    private static final String DeleteRecipientSQL = "DELETE FROM order_recipients WHERE order_recipient_id = ?";

    DBConnectionPool connectionPool = DBConnectionPool.getInstance();

    @Override
    public int create(OrderRecipient orderRecipient) {
        Connection dbConnection = connectionPool.getConnection();

        int orderRecipientId = 0;
        try (
                PreparedStatement preparedStatement = dbConnection.prepareStatement(
                        CreateRecipientSQL,
                        PreparedStatement.RETURN_GENERATED_KEYS
                )
        ) {
            preparedStatement.setInt(1, orderRecipient.getPersonId());
            preparedStatement.setInt(2, orderRecipient.getLocationId());

            SQLUtils.updateAndSetGeneratedId(preparedStatement, orderRecipient::setOrderRecipientId);

            orderRecipientId = orderRecipient.getOrderRecipientId();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionPool.releaseConnection(dbConnection);
        }
        return orderRecipientId;
    }

    @Override
    public OrderRecipient getById(int orderRecipientId) {
        Connection dbConnection = connectionPool.getConnection();

        OrderRecipient orderRecipient = null;
        try (
                PreparedStatement preparedStatement = dbConnection.prepareStatement(SelectRecipientSQL)
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

    @Override
    public void update(OrderRecipient orderRecipient) {
        Connection dbConnection = connectionPool.getConnection();

        try (
                PreparedStatement preparedStatement = dbConnection.prepareStatement(UpdateRecipientSQL)
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
                PreparedStatement preparedStatement = dbConnection.prepareStatement(DeleteRecipientSQL)
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
