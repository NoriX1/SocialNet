package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class BaseDao {
    private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";

    public BaseDao(){
        createTableIfNotExist();
    }

    protected abstract void createTableIfNotExist();

    public Connection getConnection(){
        try {
            Class.forName(DRIVER_NAME);
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/socialnetwork", "root", "");
            connection.setAutoCommit(false);
            return connection;

            } catch (SQLException  | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

}
