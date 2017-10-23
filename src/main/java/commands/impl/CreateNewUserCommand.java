package commands.impl;

import commands.Command;
import commands.Receiver;
import dao.UserDao;
import dao.impl.UserDaoImpl;
import model.Network;
import model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class CreateNewUserCommand implements Command{
    private Receiver receiver;

    public CreateNewUserCommand(Receiver receiver){
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        String answer, name, surname, login, password, sex;
        int age;
        Network network = receiver.getNetwork();
        List<User> userList = network.getUserList();
        boolean correct = true;
        System.out.println("Creating new user...");
        System.out.println("Are you want to be anonymous?(Y/N) (Login & Password = your username)");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            answer = reader.readLine();
            if ((answer.equals("n")) || (answer.equals("N"))){
                System.out.println("Please, write your name: ");
                name = reader.readLine();
                System.out.println("Please, write your surname: ");
                surname = reader.readLine();
                System.out.println("Please, write your age: ");
                age = Integer.parseInt(reader.readLine());
                System.out.println("Please, write your sex: ");
                sex = reader.readLine();
                do {
                    System.out.println("Please, write your login: ");
                    login = reader.readLine();
                    correct = true;
                    for (User i : userList){
                        if (i.getLogin().equals(login)){
                            correct = false;
                        }
                    }
                    if (!correct){
                        System.out.println("Sorry, but this login is already exist! Please, enter another login!");
                    }
                }while (!correct);
                System.out.println("Please, write your password: ");
                password = reader.readLine();
                User user = new User(network.getNumberOfUsers(),name, surname, age, sex, login, password);
                UserDao userDao = new UserDaoImpl();
                userDao.saveUser(user);
                network.addUser(user);
                System.out.println("User is added to the network, number of users in the network: "+network.getNumberOfUsers());
            }
            else
            {
                User user = new User(network.getNumberOfUsers(),"Anonymous"+network.getNumberOfUsers());
                network.addUser(user);
                UserDao userDao = new UserDaoImpl();
                userDao.saveUser(user);
                System.out.println("User is added to the network, number of users in the network: "+network.getNumberOfUsers());
            }
        } catch (IOException e){}
    }
}
