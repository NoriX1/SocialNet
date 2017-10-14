package model;

import java.util.ArrayList;
import java.util.List;

public class Network {
    private final List<User> userList = new ArrayList<>();
    private final List<Message> publicMessageList = new ArrayList<>();
    private final List<Message> privateMessageList = new ArrayList<>();

    private User currentUser;

    public void addUser(User user){
        userList.add(user);
    }

    public int getNumberOfUsers(){
        return userList.size();
    }
    public List<User> getUserList(){
        return this.userList;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public List<Message> getPublicMessageList() {
        return publicMessageList;
    }
    public void addPublicMessageInList(Message message){
        publicMessageList.add(message);
    }
    public List<Message> getPrivateMessageList() {
        return privateMessageList;
    }
    public void addPrivateMessageInList(Message message){
        privateMessageList.add(message);
    }
}
