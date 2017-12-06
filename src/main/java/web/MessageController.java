package web;

import dao.MessageDao;
import dao.UserDao;
import model.Message;
import model.User;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import services.CheckService;
import services.RatingService;
import services.impl.CheckServiceImpl;

import java.util.List;

@Controller
@RequestMapping("/")
public class MessageController {

    private final UserDao userDao;
    private final MessageDao messageDao;
    private CheckService checkService;
    private static Logger LOG = LogManager.getLogger();

    @Autowired
    public MessageController(UserDao userDao, MessageDao messageDao) {
        this.userDao = userDao;
        this.messageDao = messageDao;
        this.checkService = new CheckServiceImpl(userDao, messageDao);
    }

    @RequestMapping(path = "/id={id}/messages", method = RequestMethod.GET)
    public String showMessages(Model model, @PathVariable String id) {
        if(checkService.checkIdForErrors(id)){
            int userid = Integer.parseInt(id);
            List<Message> publicMessageList = messageDao.getPublicMessagesFromBD();
            List<Message> privateMessageList = messageDao.getPrivateMessagesFromBD(userid);
            LOG.info("PrivateMessages: {}",privateMessageList.toString());
            model.addAttribute("publicMessageList",publicMessageList);
            model.addAttribute("privateMessageList", privateMessageList);
            model.addAttribute("id", "id=");
            model.addAttribute("space", " ");
            return "messages";
        }
        else{
            return "redirect:/id={id}/login";
        }
    }
    @RequestMapping(path = "/id={id}/sendmessages", method = RequestMethod.GET)
    public String sendMessagesPage(Model model, @PathVariable String id) {
        model.addAttribute(new Message());
        if (checkService.checkIdForErrors(id)){
            List<User> userList = userDao.getUserListFromNetwork();
            model.addAttribute("userList", userList);
            model.addAttribute("id", "id=");
            model.addAttribute("space", " ");
            return "sendmessages";
        }
        else{
            return "redirect:/id={id}/login";
        }
    }

    @RequestMapping(path = "/id={id}/sendmessages", method = RequestMethod.POST)
    public String SendMessagesPost(Message message, Model model, @PathVariable String id) {
        LOG.info("Got message: {}", message.getMessage());
        int userid = 0;
        if (checkService.checkIdForErrors(id)){
            userid = Integer.parseInt(id);
        }
        else{
            if(StringUtils.equals("null", id)){
                return "redirect:/id={id}/login";
            }
        }
        User owner = userDao.findUserById(userid);
        if(messageDao.checkMessageOnPrivate(message)){
            if(!checkService.checkUserForErrors(userDao.findUserById(message.getTargetid()),false)){
                return "redirect:/id={id}/error_User is not exist";
            }
            int targetid = message.getTargetid();
            User target = userDao.findUserById(targetid);
            Message sendedmessage = new Message(owner, message.getMessage(),target, true);
            if(!messageDao.checkMessageOnErrors(sendedmessage)) return "redirect:/error_Message is blank";
            messageDao.savePrivateMessageToBD(sendedmessage);
            LOG.info("Saved private message to BD");
            return "redirect:/id={id}/sendmessages";
        }
        else{
            Message sendedmessage = new Message(owner, message.getMessage());
            if(!messageDao.checkMessageOnErrors(sendedmessage)) return "redirect:/error_Message is_blank";
            messageDao.savePublicMessageToBD(sendedmessage);
            LOG.info("Saved public message to BD");
            return "redirect:/id={id}/sendmessages";
        }
    }

    @RequestMapping(path = "/id={id}/rating", method = RequestMethod.GET)
    public String showMessageRating(Model model) {
        RatingService ratingService = new RatingService();
        List<Message> messageList = messageDao.getPublicMessagesFromBD();
        List<String> ratingList = ratingService.getRating(messageList,ratingService.getWordTop(messageList));
        model.addAttribute("ratingList", ratingList);
        return "rating";
    }
}
