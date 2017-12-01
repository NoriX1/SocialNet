package dao.impl;

import commands.Receiver;
import dao.BaseDao;
import dao.MessageDao;
import dao.UserDao;
import model.Message;
import model.User;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageDaoImpl extends BaseDao implements MessageDao{
    private static Logger LOG = LogManager.getLogger();
    private final Receiver receiver;

    public MessageDaoImpl(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    protected void createTableIfNotExist() {
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
    public List<Message> getPublicMessagesFromBD(){
        UserDao userDao = new UserDaoImpl(receiver);
        String sql = "SELECT * FROM messages WHERE isprivate = (?)";
        ResultSet resultSet;
        List<Message> messages = new ArrayList<>();
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, 0);
            resultSet = statement.executeQuery();
            while (resultSet.next()){
                messages.add(new Message(userDao.findUserById(resultSet.getInt(1)),
                        resultSet.getString(2),
                        userDao.findUserById(resultSet.getInt(3)),
                        resultSet.getBoolean(4)));
            }
            return messages;
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
    @Override
    public List<Message> getPrivateMessagesFromBD(int id){
        UserDao userDao = new UserDaoImpl(receiver);
        String sql = "SELECT * FROM messages WHERE target = (?) AND isprivate = (?)";
        ResultSet resultSet;
        List<Message> messages = new ArrayList<>();
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.setInt(2, 1);
            resultSet = statement.executeQuery();
            while (resultSet.next()){
                messages.add(new Message(userDao.findUserById(resultSet.getInt(1)),
                        resultSet.getString(2),
                        userDao.findUserById(resultSet.getInt(3)),
                        resultSet.getBoolean(4)));
            }
            return messages;
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
    public Boolean checkMessageOnPrivate(Message message) {
        try{
            int targetid = message.getTargetid();
        }catch (NullPointerException e){
            LOG.info("Cathed exception {} : it is a public message",e);
            return false;
        }
        return true;
    }

    @Override
    public Boolean checkMessageOnErrors(Message message){
        if(message.getOwner() == null) return false;
        if(StringUtils.isBlank(message.getMessage())) return false;
        if(message.isPrivate() == true){
            try{
                message.getOwner();
            }
            catch (NullPointerException e){
                LOG.info("Private message must have owner",e);
                return false;
            }
        }
        return true;
    }
}
