package services;

import commands.Command;
import commands.Invoker;
import dao.DataDao;
import dao.impl.DataDaoImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Menu {
    private final Invoker invoker;
    private final Command createNewUserCommand;
    private final Command loginCommand;
    private final Command sendPublicMessage;
    private final Command sendPrivateMessage;
    private final Command addFriendCommand;
    private final Command showAllFriendsCommand;
    private final Command findUserInNetwork;
    private final Command showAllPublicMessages;
    private final Command showAllPrivateMessages;
    private final Command showRatingCommand;
    private final DataDao dataDao;

    public Menu(Invoker invoker, Command createNewUserCommand, Command loginCommand,
                Command sendPublicMessage, Command sendPrivateMessage, Command addFriendCommand,
                Command showAllFriendsCommand, Command findUserInNetwork, Command showAllPublicMessages,
                Command showAllPrivateMessages, Command showRatingCommand, DataDao dataDao) {
        this.invoker = invoker;
        this.createNewUserCommand = createNewUserCommand;
        this.loginCommand = loginCommand;
        this.sendPublicMessage = sendPublicMessage;
        this.sendPrivateMessage = sendPrivateMessage;
        this.addFriendCommand = addFriendCommand;
        this.showAllFriendsCommand = showAllFriendsCommand;
        this.findUserInNetwork = findUserInNetwork;
        this.showAllPublicMessages = showAllPublicMessages;
        this.showAllPrivateMessages = showAllPrivateMessages;
        this.showRatingCommand = showRatingCommand;
        this.dataDao = dataDao;

    }


    public void showMenu(){
        boolean exit = false;
        dataDao.createTableIfNotExist();
        dataDao.getUsersFromBD();
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
        catch (IllegalArgumentException|IOException e){

        }

    }
}
