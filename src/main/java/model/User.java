package model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String surname;
    private int age;
    private String sex;
    private String login;
    private String password;
    private boolean islogged;
    private List<User> friendlist = new ArrayList<>();

    public User(String name){
        this.name = name;
        this.surname = "Undefinded";
        this.age = 0;
        this.sex = "Undefinded";
        this.islogged = false;
        this.login = "";
        this.password = "";
        this.islogged = false;
    }

    public User(String name, String surname, int age, String sex, String login, String password){
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.sex = sex;
        this.login = login;
        this.password = password;
        this.islogged = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isIslogged() {
        return islogged;
    }

    public void setIslogged(boolean islogged) {
        this.islogged = islogged;
    }

    public List<User> getFriendlist() {
        return friendlist;
    }

    public void addFriend(User user){
        this.friendlist.add(user);
    }
}
