package commands.impl;

import commands.Command;
import commands.Receiver;
import model.Network;
import model.User;

public class ShowAllFriendsCommand implements Command{
    Receiver receiver;
    public ShowAllFriendsCommand(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void execute() {
        Network network = receiver.getNetwork();
        User currentUser = network.getCurrentUser();
        if (currentUser == null) {
            System.out.println("You must login before watching a list of friends!");
        }
        else{
            for (User i : currentUser.getFriendlist()){
                System.out.print(i.getName()+" "+i.getSurname()+"; ");
            }
            System.out.println();
            try{Thread.sleep(2000);}catch (InterruptedException e){}

        }
    }
}
