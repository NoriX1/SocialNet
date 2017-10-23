package commands.impl;

import commands.Command;
import commands.Receiver;
import dao.DataDao;
import dao.impl.DataDaoImpl;
import model.Message;
import model.Network;
import model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class SendPublicMessage implements Command {
    private Receiver receiver;
    public SendPublicMessage(Receiver receiver){
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        Network network = receiver.getNetwork();
        List<User> userList = network.getUserList();
        User currentUser = network.getCurrentUser();
        DataDao dataDao = new DataDaoImpl();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String mess;
        try {
            if (currentUser == null) {
                System.out.println("You must login before send message!");
            } else {
                System.out.println("Enter a public message: ");
                mess = reader.readLine();
                Message message = new Message(currentUser, mess);
                network.addPublicMessageInList(message);
                dataDao.savePublicMessageToBD(message);
                System.out.println("Message sent!");
                try{Thread.sleep(2000);}catch (InterruptedException e){}

            }
        }catch (IOException e){}
    }
}
