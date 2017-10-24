package commands.impl;

import commands.Command;
import commands.Receiver;
import dao.DataDao;
import dao.impl.DataDaoImpl;
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
        DataDao dataDao = new DataDaoImpl();
        if (currentUser == null){
            System.out.println("You must login before read a private message!");
            try{Thread.sleep(3000);}catch (InterruptedException e){}
        }
        else{
            List<Message> privateList = dataDao.getPrivateMessagesFromBD(currentUser.getId());
            for (Message i : privateList){
                System.out.println(i.getOwner().getName()+" " + i.getOwner().getSurname()+" (id = "
                        +i.getOwner().getId()+") says: "+i.getMessage());

            }
            try{Thread.sleep(3000);}catch (InterruptedException e){}
        }
    }
}
