package dao.impl;
import dao.BaseDao;
import dao.MessageDao;
import dao.UserDao;
import model.Message;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//@Service
public class MessageDaoImpl extends BaseDao implements MessageDao{
    private static Logger LOG = LogManager.getLogger();

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
                    "id INT(11) PRIMARY KEY,"+
                    "owner INT(11),"+
                    "message VARCHAR(1500),"+
                    "target INT(11),"+
                    "isPrivate BOOLEAN)";
            statement.execute(createmessagetable);

            String createfriendtable = "CREATE TABLE IF NOT EXISTS friendlist ("+
                    "id INT(11) PRIMARY KEY,"+
                    "who INT(11),"+
                    "whom INT(11))";
            statement.execute(createfriendtable);

        }catch (SQLException e){
            throw new RuntimeException();
        }
    }

    @Override
    public List<Message> getPublicMessagesFromBD(){
        UserDao userDao = new UserDaoImpl();
        String sql = "SELECT * FROM messages WHERE isprivate = (?)";
        ResultSet resultSet;
        List<Message> messages = new ArrayList<>();
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, 0);
            resultSet = statement.executeQuery();
            while (resultSet.next()){
                messages.add(new Message(userDao.findUserById(resultSet.getInt(2)),
                        resultSet.getString(3),
                        userDao.findUserById(resultSet.getInt(4)),
                        resultSet.getBoolean(5)));
            }
            return messages;
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
    @Override
    public List<Message> getPrivateMessagesFromBD(int id){
        UserDao userDao = new UserDaoImpl();
        String sql = "SELECT * FROM messages WHERE target = (?) AND isprivate = (?)";
        ResultSet resultSet;
        List<Message> messages = new ArrayList<>();
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.setInt(2, 1);
            resultSet = statement.executeQuery();
            while (resultSet.next()){
                messages.add(new Message(userDao.findUserById(resultSet.getInt(2)),
                        resultSet.getString(3),
                        userDao.findUserById(resultSet.getInt(4)),
                        resultSet.getBoolean(5)));
            }
            return messages;
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void savePublicMessageToBD(Message message){

        String sql = "INSERT INTO messages (id ,owner, message, isPrivate) VALUES (?,?,?,?)";

        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ){
            statement.setInt(1,countMessagesInDB());
            statement.setInt(2,message.getOwner().getId());
            statement.setString(3, message.getMessage());
            statement.setBoolean(4,false);
            statement.execute();
            connection.commit();

        }catch (SQLException e){
            throw new RuntimeException();
        }
    }

    @Override
    public void savePrivateMessageToBD(Message message){
        String sql = "INSERT INTO messages (id ,owner, message, target, isPrivate) VALUES (?,?,?,?,?)";
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ){
            statement.setInt(1,countMessagesInDB());
            statement.setInt(2,message.getOwner().getId());
            statement.setString(3, message.getMessage());
            statement.setInt(4, message.getTarget().getId());
            statement.setBoolean(5, message.isPrivate());
            statement.execute();
            connection.commit();

        }catch (SQLException e){
            throw new RuntimeException();
        }
    }


    @Override
    public int countMessagesInDB(){
        String sql = "SELECT COUNT(1) FROM messages";
        ResultSet resultSet;
        int count = 0;
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            resultSet = statement.executeQuery();
            while (resultSet.next()){
                count = resultSet.getInt(1);

            }
            if (count == 0){
                LOG.info("count of messages = "+count);
                LOG.info("ERROR");
                throw new RuntimeException("ERROR, 0 messages in network");
            }
            else{
                LOG.info("count of messages = "+count);
                return count;
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
}
