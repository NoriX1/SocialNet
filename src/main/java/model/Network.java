package model;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Network {
    private final List<User> userList = new ArrayList<>();
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

}
