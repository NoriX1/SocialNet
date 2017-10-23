package commands.impl;

import commands.Command;
import commands.Receiver;
import model.Message;
import model.Network;
import model.User;

import java.util.List;

public class ShowAllPrivateMessages implements Command {
    Receiver receiver;
    public ShowAllPrivateMessages(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void execute() {
        Network network = receiver.getNetwork();
        User currentUser = network.getCurrentUser();
        if (currentUser == null){
            System.out.println("You must login before read a private message!");
            try{Thread.sleep(3000);}catch (InterruptedException e){}
        }
        else{
            List<Message> privateList = network.getPrivateMessageList();
            for (Message i : privateList){
                if(currentUser.getName() == i.getTarget().getName()){
                    System.out.println(i.getOwner().getName()+" " + i.getOwner().getSurname()+" (id = "
                            +i.getOwner().getId()+") says: "+i.getMessage());

                }
            }
            try{Thread.sleep(3000);}catch (InterruptedException e){}
        }
    }
}
