package dao.impl;

import dao.BaseDao;
import dao.UserDao;
import model.User;

import java.sql.*;

public class UserDaoImpl extends BaseDao implements UserDao{

    @Override
    public void createTable(){
        try(Connection connection = getConnection();
            Statement statement = connection.createStatement()
        ){
            String sql = "CREATE TABLE IF NOT EXISTS user (name VARCHAR(255) PRIMARY_KEY)";
            statement.execute(sql);


        }catch (SQLException e){
            throw new RuntimeException();
        }
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO user VALUES (?)";
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ){

            statement.setString(1, user.getName());
            statement.setString(2, "F");
            statement.execute();
            connection.commit();

        }catch (SQLException e){
            throw new RuntimeException();
        }
    }

    @Override
    public User getByName(String name) {
        String sql = "SELECT name, gender FROM user WHERE name = ?";
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ){

            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                String sex = resultSet.getString("gender");

                User user = new User(name);
                user.setSex(sex);

                return user;
            }

            throw new RuntimeException("The gender is not found");

        }catch (SQLException e){
            throw new RuntimeException();
        }
    }
}
