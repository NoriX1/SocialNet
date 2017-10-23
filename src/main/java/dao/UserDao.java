package dao;

import commands.Receiver;
import model.Message;
import model.User;

public interface UserDao {
    void createTableIfNotExist();
    void saveUser (User user);
    User getByName(String name);
    void getAllFromBD(Receiver receiver);
    void getAllMessagesFromBD(Receiver receiver);
    void savePublicMessageToBD(Message message);
    void savePrivateMessageToBD(Message message);

}
