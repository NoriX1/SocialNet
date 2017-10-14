package commands.impl;

import commands.Command;
import commands.Receiver;
import model.Network;
import model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class AddFriendCommand implements Command {
    Receiver receiver;
    public AddFriendCommand(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void execute() {
        Network network = receiver.getNetwork();
        List<User> userList = network.getUserList();
        User currentUser = network.getCurrentUser();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String name;
        boolean finded = false;
        try {
            if (currentUser == null) {
                System.out.println("You must login before add friend!");
            } else {
                System.out.println("Users in network: ");
                for(User i : userList)
                {
                    System.out.print(i.getName()+" ");
                }
                System.out.println();
                System.out.println("Enter a name of User (who will be your friend): ");
                name = reader.readLine();
                for(User i : userList) {
                    if(i.getName().equals(name)) {
                        finded = true;
                        currentUser.addFriend(i);
                        System.out.println("Friend added!");
                        try{Thread.sleep(2000);}catch (InterruptedException e){}
                        break;
                    }

                }
                if (!finded){
                    System.out.println("Sorry, but this user is not exist!");
                    try{Thread.sleep(2000);}catch (InterruptedException e){}
                }

            }
        }catch (IOException e){}
        finded = false;
    }
}
