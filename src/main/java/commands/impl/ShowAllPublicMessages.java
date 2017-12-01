//package commands.impl;
//
//import commands.Command;
//import commands.Receiver;
//import dao.DataDao;
//import dao.impl.DataDaoImpl;
//import model.Message;
//import model.Network;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class ShowAllPublicMessages implements Command {
//    Receiver receiver;
//
//    @Autowired
//    public ShowAllPublicMessages(Receiver receiver){
//        this.receiver = receiver;
//    }
//    @Override
//    public void execute() {
//        DataDao dataDao = new DataDaoImpl(receiver);
//        List<Message> ListOfMessages = dataDao.getPublicMessagesFromBD();
//        System.out.println("Public messages: ");
//        for(Message i : ListOfMessages){
//            System.out.println(i.getOwner().getName() + " " + i.getOwner().getSurname()+" (id = "
//            +i.getOwner().getId()+"): "+i.getMessage());
//        }
//        try{Thread.sleep(2000);}catch (InterruptedException e){}
//    }
//}
