package services.impl;

import dao.DataDao;
import model.Message;
import model.User;
import org.apache.commons.lang.StringUtils;
import services.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {
    private final DataDao dataDao;

    public UserServiceImpl(DataDao dataDao){
        this.dataDao = dataDao;
    }

    @Override
    public User getUserById(int id) {
        return dataDao.findUserInBD(id);
    }

    public List<Message> getListOfPrivateMessages(int id){
        return dataDao.getPrivateMessagesFromBD(id);
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
            dataDao.saveUser(user);
        }
    }
}
