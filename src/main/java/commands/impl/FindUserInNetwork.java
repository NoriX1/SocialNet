package commands.impl;

import com.sun.org.apache.xpath.internal.SourceTree;
import commands.Command;
import commands.Receiver;
import model.Network;
import model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FindUserInNetwork implements Command{
    Receiver receiver;
    public FindUserInNetwork(Receiver receiver){
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        Network network = receiver.getNetwork();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean finded = false;
        for (User i : network.getUserList()){
            System.out.print(i.getName()+" "+i.getSurname()+" id="+i.getId()+";");
        }
        System.out.println();
        System.out.println("Enter id of User: ");
        try{
            int id  = Integer.parseInt(reader.readLine());
            for(User i : network.getUserList()){
                if (i.getId()== id){
                    finded = true;
                    System.out.println(i.getId());
                    System.out.println(i.getName()+" "+i.getSurname());
                    System.out.println("Age: "+i.getAge());
                    System.out.println("Sex: "+i.getSex());
                    try{Thread.sleep(2000);}catch (InterruptedException e){}
                }
            }
            if (!finded){
                System.out.println("This user is not exist!");
                try{Thread.sleep(2000);}catch (InterruptedException e){}
            }
        }catch (IOException e){}
        finded = false;
    }
}
