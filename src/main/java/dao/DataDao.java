package dao;

import commands.Receiver;
import model.Message;
import model.User;

import java.util.List;

public interface DataDao {
    void createTableIfNotExist();
    void saveUser (User user);
    void getUsersFromBD(Receiver receiver);
    void savePublicMessageToBD(Message message);
    void savePrivateMessageToBD(Message message);
    void saveFriendToFriendlistBD(int who, int whom);
    void loadFriendListFromBD(Receiver receiver, User currentUser);
    User findUserInBD(int id);
    List<Message> getPublicMessagesFromBD();
    List<Message> getPrivateMessagesFromBD(int id);

}
