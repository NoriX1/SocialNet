package dao;

import model.User;

import java.util.List;

public interface UserDao {
    void saveUser(User user);
    User findUserById(int id);
    String userToString(User user);
    int countUserInDB();
    Boolean checkUserForErrors(User checkuser, Boolean checklogin);
    Boolean checkLoginForExisting(User checkuser);
    Boolean loggingIn(String login, String password);
    int getCurrentUserID();
    Boolean isUserLogged();
    void logoutUser();
    List<User> getUserListFromNetwork();
    void saveFriendToFriendlistBD(int who, int whom);
    void loadFriendListFromBD(User currentUser);

}
