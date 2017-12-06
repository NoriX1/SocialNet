package services;

import model.User;

public interface CheckService {
    public boolean checkIdForErrors(String id);
    public Boolean checkUserForErrors(User checkuser, Boolean checklogin);
}
