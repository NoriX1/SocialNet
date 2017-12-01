package dao;

import model.Message;

import java.util.List;

public interface MessageDao {
    List<Message> getPublicMessagesFromBD();
    List<Message> getPrivateMessagesFromBD(int id);
    void savePublicMessageToBD(Message message);
    void savePrivateMessageToBD(Message message);
    Boolean checkMessageOnPrivate(Message message);
    Boolean checkMessageOnErrors(Message message);

}
