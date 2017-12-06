package services;

import model.Message;
import model.User;

public interface CheckService {
    public boolean checkIdForErrors(String id);
    public Boolean checkUserForErrors(User checkuser, Boolean checklogin);
    public Boolean checkMessageOnPrivate(Message message);
    public Boolean checkMessageOnErrors(Message message);
}
