package ru.func.skywars.database;

/**
 * @author func 08.09.2019
 */
import java.sql.Connection;
import java.sql.SQLException;

public abstract class DatabaseConnector {

    Connection connection;

    DatabaseConnector() {
        this.connection = null;
    }

    public abstract Connection openConnection() throws SQLException, ClassNotFoundException;

    boolean checkConnection() throws SQLException {
        return connection != null && !connection.isClosed();
    }
}
