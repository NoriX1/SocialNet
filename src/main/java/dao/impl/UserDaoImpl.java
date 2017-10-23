package dao.impl;

import commands.Receiver;
import dao.BaseDao;
import dao.UserDao;
import model.Message;
import model.Network;
import model.User;

import java.rmi.RemoteException;
import java.sql.*;
import java.util.List;

public class UserDaoImpl extends BaseDao implements UserDao{

    @Override
    public void createTableIfNotExist(){
        try(Connection connection = getConnection();
            Statement statement = connection.createStatement()
        ){
            String createusertable = "CREATE TABLE IF NOT EXISTS users (" +
                    "name VARCHAR(30)," +
                    "surname VARCHAR(30),"+
                    "age INT(4),"+
                    "sex VARCHAR(7),"+
                    "login VARCHAR(30),"+
                    "password VARCHAR(30))";

            statement.execute(createusertable);

            String createmessagetable = "CREATE TABLE IF NOT EXISTS messages ("+
                    "owner INT(11),"+
                    "message VARCHAR(1500),"+
                    "target INT(11),"+
                    "isPrivate BOOLEAN)";
            statement.execute(createmessagetable);

        }catch (SQLException e){
            throw new RuntimeException();
        }

    }

    @Override
    public void saveUser(User user) {
        String sql = "INSERT INTO users (id, name, surname, age, sex, login, password) VALUES (?,?,?,?,?,?,?)";
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ){

            statement.setInt(1,user.getId());
            statement.setString(2, user.getName());
            statement.setString(3, user.getSurname());
            statement.setInt(4, user.getAge());
            statement.setString(5, user.getSex());
            statement.setString(6, user.getLogin());
            statement.setString(7, user.getPassword());
            statement.execute();
            connection.commit();

        }catch (SQLException e){
            throw new RuntimeException();
        }
    }

    @Override
    public void getAllFromBD(Receiver receiver){
        Network network = receiver.getNetwork();
        String sql = "SELECT * FROM users";
        String sqlmessages = "SELECT * FROM messages";
        ResultSet resultSet;
        try(Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
            resultSet = statement.executeQuery();
            while (resultSet.next()){
                network.addUser(new User(resultSet.getInt(1), resultSet.getString(2),
                        resultSet.getString(3), resultSet.getInt(4),
                        resultSet.getString(5), resultSet.getString(6),
                        resultSet.getString(7)));
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sqlmessages)) {
            resultSet = statement.executeQuery();
            List<User> userList = network.getUserList();
            int owner, target;
            String message;
            while (resultSet.next()){
                owner = resultSet.getInt(1);
                message = resultSet.getString(2);
                if(resultSet.getBoolean(4)){
                    target = resultSet.getInt(3);
                    for(User i: userList){
                        if(i.getId() == owner){
                            for (User j: userList){
                                if(j.getId() == target){
                                    network.addPrivateMessageInList(new Message(i,message,j));
                                }
                            }
                        }
                    }
                }
                else if (!resultSet.getBoolean(4)){
                    for(User i:userList){
                        if(i.getId() == owner){
                            network.addPublicMessageInList(new Message(i,message));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void getAllMessagesFromBD(Receiver receiver){
        Network network = receiver.getNetwork();
    }

    @Override
    public void savePublicMessageToBD(Message message){

        String sql = "INSERT INTO messages (owner, message, isPrivate) VALUES (?,?,?)";

        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ){
            statement.setInt(1,message.getOwner().getId());
            statement.setString(2, message.getMessage());
            statement.setBoolean(3,false);
            statement.execute();
            connection.commit();

        }catch (SQLException e){
            throw new RuntimeException();
        }
    }

    @Override
    public void savePrivateMessageToBD(Message message){
        String sql = "INSERT INTO messages (owner, message, target, isPrivate) VALUES (?,?,?,?)";
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ){
            statement.setInt(1,message.getOwner().getId());
            statement.setString(2, message.getMessage());
            statement.setInt(3, message.getTarget().getId());
            statement.setBoolean(4, message.isPrivate());
            statement.execute();
            connection.commit();

        }catch (SQLException e){
            throw new RuntimeException();
        }
    }

    @Override
    public User getByName(String name) {
        String sql = "SELECT name, sex FROM user WHERE name = ?";
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ){

            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                String sex = resultSet.getString("sex");

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
