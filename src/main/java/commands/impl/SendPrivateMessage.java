package commands.impl;

import commands.Command;
import commands.Receiver;
import model.Message;
import model.Network;
import model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class SendPrivateMessage implements Command{
    private Receiver receiver;
    public SendPrivateMessage(Receiver receiver) {
        this.receiver = receiver;
    }
    @Override
    public void execute() {
        Network network = receiver.getNetwork();
        List<User> userList = network.getUserList();
        User currentUser = network.getCurrentUser();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String mess, name;
        boolean finded = false;
        try {
            if (currentUser == null) {
                System.out.println("You must login before send message!");
            } else {
                System.out.println("Users in network: ");
                for(User i : userList)
                {
                    System.out.print(i.getName()+" ");
                }
                System.out.println();
                System.out.println("Enter a name of User (who will be sent a message): ");
                name = reader.readLine();
                for(User i : userList)
                {
                    if(i.getName().equals(name)) {
                        finded = true;
                        System.out.println("Enter a private message: ");
                        mess = reader.readLine();
                        Message message = new Message(currentUser, mess, i);
                        network.addPrivateMessageInList(message);
                        System.out.println("Message sent!");
                        try{Thread.sleep(2000);}catch (InterruptedException e){}
                    }
                }
                if(!finded){
                    System.out.println("Sorry, but this user is not exist!");
                    try{Thread.sleep(2000);}catch (InterruptedException e){}
                }
            }
        }catch (IOException e){}
        finded = false;
    }

}