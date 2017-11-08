package commands.impl;

import commands.Command;
import commands.Receiver;
import dao.DataDao;
import dao.impl.DataDaoImpl;
import model.Network;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShowAllFriendsCommand implements Command{
    private Receiver receiver;

    @Autowired
    public ShowAllFriendsCommand(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void execute() {
        Network network = receiver.getNetwork();
        DataDao dataDao = new DataDaoImpl(receiver);
        User currentUser = network.getCurrentUser();
        if (currentUser == null) {
            System.out.println("You must login before watching a list of friends!");
        }
        else{
            dataDao.loadFriendListFromBD(currentUser);
            for (User i : currentUser.getFriendlist()){
                System.out.print(i.getName()+" "+i.getSurname()+" (id = "+ i.getId()+"); ");
            }
            System.out.println();
            try{Thread.sleep(2000);}catch (InterruptedException e){}

        }
    }
}
