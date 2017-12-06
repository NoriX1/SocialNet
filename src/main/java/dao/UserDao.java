package dao;

import model.User;

import java.util.List;

public interface UserDao {
    void saveUser(User user);
    User findUserById(int id);
    String userToString(User user);
    int countUserInDB();
    Boolean checkLoginForExisting(User checkuser);
    String loggingIn(String login, String password);
    List<User> getUserListFromNetwork();
    void saveFriendToFriendlistBD(int who, int whom);
    public List<User> loadFriendListFromBD(int userID);
    public int countFriendsInDB();

}
