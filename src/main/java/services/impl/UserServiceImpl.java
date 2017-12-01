package services.impl;

import dao.DataDao;
import dao.MessageDao;
import dao.UserDao;
import model.Message;
import model.User;
import org.apache.commons.lang.StringUtils;
import services.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {
    private final MessageDao messageDao;
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao, MessageDao messageDao){
        this.userDao = userDao;
        this.messageDao = messageDao;

    }

    @Override
    public User getUserById(int id) {
        return userDao.findUserById(id);
    }

    public List<Message> getListOfPrivateMessages(int id){
        return messageDao.getPrivateMessagesFromBD(id);
    }
    public String getNameOfUserById(int id){
        User user = getUserById(id);
        return user.getName();
    }
    public void changeNameOfUser(int id, String newname){
        User user = getUserById(id);
        if(StringUtils.isBlank(newname)) {
            System.out.println("Your name is incorrect!");
        }
        else {
            user.setName(newname);
            userDao.saveUser(user);
        }
    }
}
