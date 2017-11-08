package commands;

import org.springframework.stereotype.Service;

@Service
public class Invoker {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void run(){
        command.execute();
    }

}
