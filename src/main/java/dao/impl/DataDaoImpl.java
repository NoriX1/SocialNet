package dao.impl;

import commands.Receiver;
import dao.BaseDao;
import dao.DataDao;
import model.Message;
import model.Network;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataDaoImpl extends BaseDao implements DataDao {
    private final Receiver receiver;

    public DataDaoImpl(Receiver receiver){
        this.receiver = receiver;
    }

    @Override
    public void createTableIfNotExist(){
        try(Connection connection = getConnection();
            Statement statement = connection.createStatement()
        ){
            String createusertable = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT(11) PRIMARY KEY," +
                    "name VARCHAR(30)," +
                    "surname VARCHAR(30),"+
                    "age INT(4),"+
                    "sex VARCHAR(7),"+
                    "login VARCHAR(30) UNIQUE,"+
                    "password VARCHAR(30))";

            statement.execute(createusertable);

            String createmessagetable = "CREATE TABLE IF NOT EXISTS messages ("+
                    "owner INT(11),"+
                    "message VARCHAR(1500),"+
                    "target INT(11),"+
                    "isPrivate BOOLEAN)";
            statement.execute(createmessagetable);

            String createfriendtable = "CREATE TABLE IF NOT EXISTS friendlist ("+
                    "who INT(11),"+
                    "whom INT(11))";
            statement.execute(createfriendtable);

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
    public void getUsersFromBD(){
        Network network = receiver.getNetwork();
        String sql = "SELECT * FROM users";
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
    public void saveFriendToFriendlistBD(int who, int whom){
        String sql = "INSERT INTO friendlist (who, whom) VALUES (?,?)";
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ){
            statement.setInt(1,who);
            statement.setInt(2, whom);
            statement.execute();
            connection.commit();

        }catch (SQLException e){
            throw new RuntimeException();
        }
    }

    @Override
    public void loadFriendListFromBD(User currentUser){
        String sql = "SELECT * FROM friendlist WHERE who = (?)";
        ResultSet resultSet;
        Network network = receiver.getNetwork();
        int id;
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, currentUser.getId());
            resultSet = statement.executeQuery();
            boolean finded = false;
            while (resultSet.next()){
                id = resultSet.getInt(2);
                for (User i : currentUser.getFriendlist())
                {
                    if(i.getId() == id){
                        finded = true;
                    }
                }
                if (!finded){
                    for(User i : network.getUserList()){
                        if (i.getId() == id){
                            currentUser.addFriend(i);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }

    }

    @Override
    public User findUserInBD(int id){
        String sql = "SELECT * FROM users WHERE id = (?)";
        ResultSet resultSet;
        User user = new User();
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            while (resultSet.next()){
                user = new User(resultSet.getInt(1), resultSet.getString(2),
                        resultSet.getString(3), resultSet.getInt(4),
                        resultSet.getString(5), resultSet.getString(6),
                        resultSet.getString(7));

            }
            if (user.getLogin() == ""){
                System.out.println("Error, this user is not exit in DB");
                return null;
            }
            else
            {
                return user;
            }

        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public List<Message> getPublicMessagesFromBD(){
        String sql = "SELECT * FROM messages WHERE isprivate = (?)";
        ResultSet resultSet;
        List<Message> messages = new ArrayList<>();
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, 0);
            resultSet = statement.executeQuery();
            while (resultSet.next()){
                messages.add(new Message(findUserInBD(resultSet.getInt(1)),
                        resultSet.getString(2),
                        findUserInBD(resultSet.getInt(3)),
                        resultSet.getBoolean(4)));
            }
            return messages;
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public List<Message> getPrivateMessagesFromBD(int id){
        String sql = "SELECT * FROM messages WHERE target = (?) AND isprivate = (?)";
        ResultSet resultSet;
        List<Message> messages = new ArrayList<>();
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.setInt(2, 1);
            resultSet = statement.executeQuery();
            while (resultSet.next()){
                messages.add(new Message(findUserInBD(resultSet.getInt(1)),
                        resultSet.getString(2),
                        findUserInBD(resultSet.getInt(3)),
                        resultSet.getBoolean(4)));
            }
            return messages;
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
}
