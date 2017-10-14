package commands.impl;

import commands.Command;
import commands.Receiver;
import model.Message;
import model.Network;
import services.RatingService;

import java.util.List;

public class ShowRatingCommand implements Command{
    private Receiver receiver;
    public ShowRatingCommand(Receiver receiver){
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        Network network = receiver.getNetwork();
        List<Message> messageList = network.getPublicMessageList();
        RatingService ratingService = new RatingService();
        List<String> rating = ratingService.getRating(messageList,ratingService.getWordTop(messageList));
        for (String s : rating){
            System.out.println(s);
        }
    }
}
