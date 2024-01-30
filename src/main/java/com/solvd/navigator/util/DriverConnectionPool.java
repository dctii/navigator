package com.solvd.navigator.util;

import com.solvd.navigator.bin.Driver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DriverConnectionPool {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.DRIVER_CONNECTION_POOL);
    private static DriverConnectionPool driverConnectionPool = null;
    private final Queue<DriverConnection> driverConnections = new LinkedList<>();

    private DriverConnectionPool(List<Driver> drivers) {
        drivers.forEach(driver -> {
            String connectionName = "Driver " + driver.getDriverId();
            DriverConnection connection = new DriverConnection(connectionName);
            driverConnections.add(connection);
        });
    }

    public static synchronized DriverConnectionPool getInstance(List<Driver> drivers) {
        if (driverConnectionPool == null) {
            driverConnectionPool = new DriverConnectionPool(drivers);
        }
        return driverConnectionPool;
    }

    public synchronized DriverConnection getConnection() {
        while (driverConnections.isEmpty()) {
            try {
                /*
                    Thread: currentThread
                    https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html#currentThread--

                    Thread: getDriverName
                    https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html#getName--
                */
                String currentThreadName = StringFormatters.nestInSingleQuotations(
                        Thread.currentThread().getName()
                );

                LOGGER.info(
                        "{}{} is waiting for a connection.{}",
                        AnsiCodes.YELLOW,
                        currentThreadName,
                        AnsiCodes.RESET_ALL
                );
                wait();
            } catch (InterruptedException e) {
                /*
                    Thread: interrupt
                    https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html#interrupt--
                */
                Thread.currentThread().interrupt();
            }
        }
        /*
            "Queue<E>: poll"
            https://docs.oracle.com/javase/8/docs/api/java/util/Queue.html#poll--
         */
        return driverConnections.poll();
    }

    public synchronized void releaseConnection(DriverConnection connection) {
        driverConnections.offer(connection);
        
        String connectionName = StringFormatters.nestInSingleQuotations(
                connection.getDriverName()
        );

        LOGGER.info(
                "{}{} released from the {}{}",
                AnsiCodes.BG_RED + AnsiCodes.BOLD,
                connectionName,
                ClassConstants.DRIVER_CONNECTION_POOL.getSimpleName(),
                AnsiCodes.RESET_ALL
        );

        notifyAll();
    }


    public String toString() {
        Class<?> currClass = ClassConstants.DRIVER_CONNECTION_POOL;
        String[] fieldNames = {
                "driverConnectionPool",
                "driverConnections"
        };

        String fieldsString =
                StringFormatters.buildFieldsString(this, fieldNames);

        return StringFormatters.buildToString(currClass, fieldNames, fieldsString);
    }
}
