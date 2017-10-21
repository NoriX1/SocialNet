package dao;

import model.User;

public interface UserDao {
    void save (User user);
    User getByName(String name);

}
