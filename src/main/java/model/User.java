package model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
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
        this.sex = "Empty";
        this.islogged = false;
        this.login = name;
        this.password = name;
        this.islogged = false;
    }
    public User(int id, String name){
        this.id = id;
        this.name = name;
        this.surname = "Undefinded";
        this.age = 0;
        this.sex = "Empty";
        this.islogged = false;
        this.login = name;
        this.password = name;
        this.islogged = false;
    }

    public User(int id, String name, String surname, int age, String sex, String login, String password){
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.sex = sex;
        this.login = login;
        this.password = password;
        this.islogged = false;
    }

    public User(){
        this.name = "";
        this.surname = "";
        this.age = 0;
        this.sex = "";
        this.islogged = false;
        this.login = "";
        this.password = "";
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (age != user.age) return false;
        if (islogged != user.islogged) return false;
        if (!name.equals(user.name)) return false;
        if (!surname.equals(user.surname)) return false;
        if (!sex.equals(user.sex)) return false;
        if (!login.equals(user.login)) return false;
        if (!password.equals(user.password)) return false;
        return friendlist.equals(user.friendlist);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + surname.hashCode();
        result = 31 * result + age;
        result = 31 * result + sex.hashCode();
        result = 31 * result + login.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + (islogged ? 1 : 0);
        result = 31 * result + friendlist.hashCode();
        return result;
    }
}
