package commands.impl;

import commands.Command;
import commands.Receiver;
import dao.DataDao;
import dao.impl.DataDaoImpl;
import model.Message;
import model.Network;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import services.RatingService;

import java.util.List;

@Service
public class ShowRatingCommand implements Command{
    private Receiver receiver;

    @Autowired
    public ShowRatingCommand(Receiver receiver){
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        DataDao dataDao = new DataDaoImpl(receiver);
        List<Message> messageList = dataDao.getPublicMessagesFromBD();
        RatingService ratingService = new RatingService();
        List<String> rating = ratingService.getRating(messageList,ratingService.getWordTop(messageList));
        for (String s : rating){
            System.out.println(s);
        }
    }
}
