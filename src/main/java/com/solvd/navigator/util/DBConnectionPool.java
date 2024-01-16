package com.solvd.navigator.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.IntStream;

public class DBConnectionPool {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.DB_CONNECTION_POOL);
    private static DBConnectionPool dbConnectionPool = null;
    private static final int MAX_CONNECTIONS = 30;

    private final Queue<java.sql.Connection> dbConnections = new LinkedList<>();

    private DBConnectionPool() {
        IntStream.range(0, MAX_CONNECTIONS).forEach(i -> {
            java.sql.Connection dbConnection = new AuthConnection().getConnection();
            dbConnections.add(dbConnection);
        });
    }

    public static synchronized DBConnectionPool getInstance() {
        if (dbConnectionPool == null) {
            dbConnectionPool = new DBConnectionPool();
        }
        return dbConnectionPool;
    }

    public synchronized java.sql.Connection getConnection() {
        while (dbConnections.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        /*
            "Queue<E>: poll"
            https://docs.oracle.com/javase/8/docs/api/java/util/Queue.html#poll--
         */
        return dbConnections.poll();
    }

    public synchronized void releaseConnection(java.sql.Connection dbConnection) {
        dbConnections.offer(dbConnection);
        notifyAll();
    }


    public synchronized void shutdown() {
        dbConnections.forEach(dbConnection -> {
            try {
                dbConnection.close();
            } catch (SQLException e) {
                LOGGER.error("Error closing connection", e);
            }
        });

        dbConnections.clear();
    }


    public String toString() {
        Class<?> currClass = ClassConstants.DB_CONNECTION_POOL;
        String[] fieldNames = {
                "dbConnections"
        };

        String fieldsString =
                StringFormatters.buildFieldsString(this, fieldNames);

        return StringFormatters.buildToString(currClass, fieldNames, fieldsString);
    }
}
