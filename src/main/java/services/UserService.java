package services;

import model.Message;
import model.User;

import java.util.List;

public interface UserService {
    User getUserById(int id);
    List<Message> getListOfPrivateMessages(int id);
    String getNameOfUserById(int id);
    void changeNameOfUser(int id, String newname);
}
