package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class BaseDao {
    private static final String DRIVER_NAME = "org.h2.driver";

    public BaseDao(){
        createTable();
    }
    protected abstract void createTable();

    public Connection getConnection(){
        try {
            Class.forName(DRIVER_NAME);
                DriverManager.getConnection("jdbc:h2:-/social", "sa", "");

            } catch (SQLException  | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

}
