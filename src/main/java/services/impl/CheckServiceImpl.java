package services.impl;

import dao.MessageDao;
import dao.UserDao;
import model.Message;
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
