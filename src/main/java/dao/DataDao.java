package dao;

import commands.Receiver;
import model.Message;
import model.User;

public interface DataDao {
    void createTableIfNotExist();
    void saveUser (User user);
    void getAllFromBD(Receiver receiver);
    void savePublicMessageToBD(Message message);
    void savePrivateMessageToBD(Message message);
    void saveFriendToFriendlistBD(int who, int whom);
    void loadFriendListFromBD(Receiver receiver, User currentUser);

}
