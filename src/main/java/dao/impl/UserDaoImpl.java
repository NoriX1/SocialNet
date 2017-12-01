package dao.impl;

import commands.Receiver;
import dao.BaseDao;
import dao.DataDao;
import dao.UserDao;
import model.Network;
import model.User;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.nio.ch.Net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.List;

@Service
public class UserDaoImpl extends BaseDao implements UserDao {
    private static Logger LOG = LogManager.getLogger();
    private final Receiver receiver;


    @Autowired
    public UserDaoImpl(Receiver receiver){
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
            LOG.info("User has been saved to database");
        }catch (SQLException e){
            throw new RuntimeException();
        }

    }

    @Override
    public User findUserById(int id) {
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
                LOG.info("Error, user with id = {} is not exist", id);
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
    public String userToString(User user){
        return new String( "id="+user.getId()+",Name: "+user.getName()+",Surname: "+
                user.getSurname()+",Age: "+user.getAge()+",Sex: "+user.getSex());
    }

    @Override
    public int countUserInDB(){
        String sql = "SELECT COUNT(1) FROM users";
        ResultSet resultSet;
        int count = 0;
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            resultSet = statement.executeQuery();
            while (resultSet.next()){
                count = resultSet.getInt(1);

            }
            if (count == 0){
                LOG.info("count of users = "+count);
                LOG.info("ERROR");
                throw new RuntimeException("ERROR, 0 users in network");
            }
            else{
                LOG.info("count of users = "+count);
                return count;
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public Boolean checkLoginForExisting(User checkuser){
        User user = new User();
        String sql = "SELECT * FROM users WHERE login = (?)";
        ResultSet resultSet;
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, checkuser.getLogin());
            resultSet = statement.executeQuery();
            while (resultSet.next()){
                user = new User(resultSet.getInt(1), resultSet.getString(2),
                        resultSet.getString(3), resultSet.getInt(4),
                        resultSet.getString(5), resultSet.getString(6),
                        resultSet.getString(7));

            }
            LOG.info(user.getLogin());
            if (user.getLogin() != ""){
                LOG.info("ERROR: Login {} is exist", user.getLogin());
                return false;
            }
            else
            {
                return true;
            }

        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public Boolean checkUserForErrors(User checkuser, Boolean checklogin){
        if (checkuser == null){
            LOG.info("ERROR: User is null");
            return false;
        }
        String login = checkuser.getLogin();
        String name = checkuser.getName();
        String password = checkuser.getPassword();
        String surname = checkuser.getSurname();
        if (StringUtils.isBlank(login) || StringUtils.isBlank(name) ||
                StringUtils.isBlank(password) || StringUtils.isBlank(surname) ){
            LOG.info("ERROR: User {} is broken!", checkuser);
            return false;
        }
        if(checklogin){
            if(checkLoginForExisting(checkuser)){
                return true;
            }
            else
                return false;
        }
        else return true;
    }

    @Override
    public Boolean loggingIn(String login, String password){
        {
            DataDao dataDao = new DataDaoImpl(receiver);
            dataDao.getUsersFromBD();
            LOG.info("Logging in: ");
            boolean finded = false;
            Network network = receiver.getNetwork();
            List<User> userList = network.getUserList();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            for(User i : userList){
                if (i.getLogin().toLowerCase().equals(login.toLowerCase())){
                    finded = true;
                    if(i.getPassword().equals(password)){
                        i.setIslogged(true);
                        network.setCurrentUser(i);
                        LOG.info("User {} is logged", i);
                        return true;
                    }
                    else{
                        LOG.info("Incorrect password");
                        return false;

                    }
                }
            }
            if (!finded) {
                LOG.info("Login does not exist!");
                return false;
            }
            finded = false;
        }
        return false;
    }

    @Override
    public Boolean isUserLogged(){
        Network network = receiver.getNetwork();
        try{
            User user = network.getCurrentUser();
            LOG.info("Finded Logged User: {} ({})", user, user.toString());
        }catch (NullPointerException e){
            LOG.info("Cathed exception {} : no one is logged.",e);
            return false;
        }
        return true;
    }

    @Override
    public int getCurrentUserID(){
        Network network = receiver.getNetwork();
        User user = network.getCurrentUser();
        return user.getId();
    }

    @Override
    public void logoutUser(){
        Network network = receiver.getNetwork();
        network.setCurrentUser(null);
        LOG.info("User logged out");
    }

    @Override
    public List<User> getUserListFromNetwork(){
        DataDao dataDao = new DataDaoImpl(receiver);
        dataDao.getUsersFromBD();
        Network network = receiver.getNetwork();
        return network.getUserList();
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
}
