import commands.Command;
import commands.Invoker;
import commands.Receiver;
import commands.impl.*;
import dao.DataDao;
import dao.impl.DataDaoImpl;
import model.Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Starter {

    public static void main(String[] args) throws IOException {
        Receiver receiver = new Receiver(new Network());
        Command createNewUserCommand = new CreateNewUserCommand(receiver);
        Command loginCommand = new LoginCommand(receiver);
        Command sendPublicMessage = new SendPublicMessage(receiver);
        Command sendPrivateMessage = new SendPrivateMessage(receiver);
        Command addFriendCommand = new AddFriendCommand(receiver);
        Command showAllFriendsCommand = new ShowAllFriendsCommand(receiver);
        Command findUserInNetwork = new FindUserInNetwork(receiver);
        Command showAllPublicMessages = new ShowAllPublicMessages(receiver);
        Command showAllPrivateMessages = new ShowAllPrivateMessages(receiver);
        Command showRatingCommand = new ShowRatingCommand(receiver);
        Invoker invoker = new Invoker();
        boolean exit = false;

        DataDao dataDao = new DataDaoImpl();
        dataDao.createTableIfNotExist();
        dataDao.getUsersFromBD(receiver);




        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            do {
                System.out.println("Menu: ");
                System.out.println("0) Create New User");
                System.out.println("1) Login");
                System.out.println("2) Send Public Message");
                System.out.println("3) Send Private Message");
                System.out.println("4) Add a Friend");
                System.out.println("5) Show List of Friends");
                System.out.println("6) Find User in Network");
                System.out.println("7) Show all public messages");
                System.out.println("8) Show all private messages");
                System.out.println("9) Show Rating of Messages");
                System.out.println("10) Exit");

                int i = Integer.parseInt(reader.readLine());
                switch (i) {
                    case 0:
                        invoker.setCommand(createNewUserCommand);
                        invoker.run();
                        break;
                    case 1:
                        invoker.setCommand(loginCommand);
                        invoker.run();
                        break;
                    case 2:
                        invoker.setCommand(sendPublicMessage);
                        invoker.run();
                        break;
                    case 3:
                        invoker.setCommand(sendPrivateMessage);
                        invoker.run();
                        break;
                    case 4:
                        invoker.setCommand(addFriendCommand);
                        invoker.run();
                        break;
                    case 5:
                        invoker.setCommand(showAllFriendsCommand);
                        invoker.run();
                        break;
                    case 6:
                        invoker.setCommand(findUserInNetwork);
                        invoker.run();
                        break;
                    case 7:
                        invoker.setCommand(showAllPublicMessages);
                        invoker.run();
                        break;
                    case 8:
                        invoker.setCommand(showAllPrivateMessages);
                        invoker.run();
                        break;
                    case 9:
                        invoker.setCommand(showRatingCommand);
                        invoker.run();
                        break;
                    case 10:
                        exit = true;
                        break;
                    default:
                        throw new IllegalArgumentException(" ");
                }
            } while (!exit);
        }
        catch (IllegalArgumentException e){

        }
    }
}
