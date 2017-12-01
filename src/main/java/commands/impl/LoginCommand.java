//package commands.impl;
//
//import commands.Command;
//import commands.Receiver;
//import model.Network;
//import model.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.List;
//
//@Service
//public class LoginCommand implements Command{
//    private Receiver receiver;
//
//    @Autowired
//    public LoginCommand(Receiver receiver) {
//        this.receiver = receiver;
//    }
//
//    @Override
//    public void execute() {
//        System.out.println("Logging in: ");
//        System.out.println("Please, write your login: ");
//        String login, password;
//        boolean finded = false;
//        Network network = receiver.getNetwork();
//        List<User> userList = network.getUserList();
//        try{
//            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//            login = reader.readLine();
//            for(User i : userList){
//                if (i.getLogin().toLowerCase().equals(login.toLowerCase())){
//                    finded = true;
//                    System.out.println("Please, write your password: ");
//                    password = reader.readLine();
//                    if(i.getPassword().equals(password)){
//                        System.out.println("Hello, "+i.getName());
//                        i.setIslogged(true);
//                        network.setCurrentUser(i);
//                        break;
//                    }
//                    else{
//                        System.out.println("Incorrect password!");
//                        try{Thread.sleep(2000);}catch (InterruptedException e){}
//                    }
//                }
//            }
//            if (!finded) {
//                System.out.println("Sorry, but this login does not exist!");
//                try{Thread.sleep(2000);}catch (InterruptedException e){}
//            }
//        }catch (IOException e){}
//        finded = false;
//
//    }
//}
