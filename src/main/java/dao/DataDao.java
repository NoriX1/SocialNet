package dao;

import commands.Receiver;
import model.Message;
import model.User;

import java.util.List;

public interface DataDao {
    void createTableIfNotExist();
    void saveUser (User user);
    void getUsersFromBD();
    void savePublicMessageToBD(Message message);
    void savePrivateMessageToBD(Message message);
    void saveFriendToFriendlistBD(int who, int whom);
    void loadFriendListFromBD(User currentUser);
    User findUserInBD(int id);
    List<Message> getPublicMessagesFromBD();
    List<Message> getPrivateMessagesFromBD(int id);

}
