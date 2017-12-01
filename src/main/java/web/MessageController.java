package web;

import dao.MessageDao;
import dao.UserDao;
import dao.impl.UserDaoImpl;
import model.Message;
import model.Network;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import services.RatingService;

import java.util.List;

@Controller
@RequestMapping("/")
public class MessageController {

    private final UserDao userDao;
    private final MessageDao messageDao;
    private static Logger LOG = LogManager.getLogger();

    @Autowired
    public MessageController(UserDao userDao, MessageDao messageDao) {
        this.userDao = userDao;
        this.messageDao = messageDao;
    }

    @RequestMapping(path = "/messages", method = RequestMethod.GET)
    public String showMessages(Model model) {
        if (userDao.isUserLogged()){
            int id = userDao.getCurrentUserID();
            List<Message> publicMessageList = messageDao.getPublicMessagesFromBD();
            List<Message> privateMessageList = messageDao.getPrivateMessagesFromBD(id);
            LOG.info("PrivateMessages: {}",privateMessageList.toString());
            model.addAttribute("publicMessageList",publicMessageList);
            model.addAttribute("privateMessageList", privateMessageList);
            model.addAttribute("id", "id=");
            model.addAttribute("space", " ");
            return "messages";
        }
        else{
            return "redirect:/login";
        }
    }
    @RequestMapping(path = "/sendmessages", method = RequestMethod.GET)
    public String sendMessagesPage(Model model) {
        model.addAttribute(new Message());
        if (userDao.isUserLogged()){
            List<User> userList = userDao.getUserListFromNetwork();
            model.addAttribute("userList", userList);
            model.addAttribute("id", "id=");
            model.addAttribute("space", " ");
            return "sendmessages";
        }
        else{
            return "redirect:/login";
        }
    }

    @RequestMapping(path = "/sendmessages", method = RequestMethod.POST)
    public String SendMessagesPost(Message message, Model model) {
        LOG.info("Got message: {}", message.getMessage());
        User owner = userDao.findUserById(userDao.getCurrentUserID());
        if(messageDao.checkMessageOnPrivate(message)){
            if(!userDao.checkUserForErrors(userDao.findUserById(message.getTargetid()),false)){
                return "redirect:/error_User is not exist";
            }
            int targetid = message.getTargetid();
            User target = userDao.findUserById(targetid);
            Message sendedmessage = new Message(owner, message.getMessage(),target, true);
            if(!messageDao.checkMessageOnErrors(sendedmessage)) return "redirect:/error_Message is blank";
            messageDao.savePrivateMessageToBD(sendedmessage);
            LOG.info("Saved private message to BD");
            return "redirect:/sendmessages";
        }
        else{
            Message sendedmessage = new Message(owner, message.getMessage());
            if(!messageDao.checkMessageOnErrors(sendedmessage)) return "redirect:/error_Message is_blank";
            messageDao.savePublicMessageToBD(sendedmessage);
            LOG.info("Saved public message to BD");
            return "redirect:/sendmessages";
        }
    }

    @RequestMapping(path = "/rating", method = RequestMethod.GET)
    public String showMessageRating(Model model) {
        RatingService ratingService = new RatingService();
        List<Message> messageList = messageDao.getPublicMessagesFromBD();
        List<String> ratingList = ratingService.getRating(messageList,ratingService.getWordTop(messageList));
        model.addAttribute("ratingList", ratingList);
        return "rating";
    }
}
