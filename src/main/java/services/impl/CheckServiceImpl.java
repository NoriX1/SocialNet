package services.impl;

import dao.MessageDao;
import dao.UserDao;
import model.Network;
import model.User;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.CheckService;

public class CheckServiceImpl implements CheckService {
    private final MessageDao messageDao;
    private final UserDao userDao;
    private static Logger LOG = LogManager.getLogger();

    public CheckServiceImpl(UserDao userDao, MessageDao messageDao){
        this.userDao = userDao;
        this.messageDao = messageDao;
    }

    @Override
    public boolean checkIdForErrors(String id){
        if (StringUtils.equals("null", id)) return false;
        try{
            int test = Integer.parseInt(id);
            return true;
        }catch (NumberFormatException e){
            LOG.info("id of user have mistake {}", e);
            return false;
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
            if(userDao.checkLoginForExisting(checkuser)){
                return true;
            }
            else
                return false;
        }
        else return true;
    }
}
