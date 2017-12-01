package dao.impl;

import commands.Receiver;
import dao.BaseDao;
import dao.DataDao;
import model.Message;
import model.Network;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class DataDaoImpl extends BaseDao implements DataDao {
    private final Receiver receiver;

    @Autowired
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
    public void getUsersFromBD(){
        Network network = receiver.getNetwork();
        String sql = "SELECT * FROM users";
        ResultSet resultSet;
        Boolean finded = false;
        try(Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
            resultSet = statement.executeQuery();
            while (resultSet.next()){
                finded = false;
                User user = new User(resultSet.getInt(1), resultSet.getString(2),
                        resultSet.getString(3), resultSet.getInt(4),
                        resultSet.getString(5), resultSet.getString(6),
                        resultSet.getString(7));
                for(User i : network.getUserList()){
                    if (i.getId() == user.getId()){
                        finded = true;
                    }
                }
                if(finded == false){
                    network.addUser(user);
                }
                else{
                    continue;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
}
