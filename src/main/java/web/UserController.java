package web;

import dao.MessageDao;
import dao.UserDao;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import services.CheckService;
import services.impl.CheckServiceImpl;

import java.util.List;

@Controller
@RequestMapping("/")
public class UserController {
    private static Logger LOG = LogManager.getLogger();
    private final UserDao userDao;
    private final MessageDao messageDao;
    private CheckService checkService;

    @Autowired
    public UserController(UserDao userDao, MessageDao messageDao) {
        this.userDao = userDao;
        this.messageDao = messageDao;
        checkService = new CheckServiceImpl(userDao,messageDao);
    }

    @RequestMapping(path = "/id={id}/login", method = RequestMethod.GET)
    public String loginToSite(Model model, @PathVariable String id) {
        if(!checkService.checkIdForErrors(id)){
            model.addAttribute(new User());
            LOG.info("Need autorisation, open login");
            return "login";
        }
        else
        {
            LOG.info("Already authorized, redirect profile");
            return "redirect:/id={id}/profile";
        }
    }

    @RequestMapping(path = "/id=null/login", method = RequestMethod.POST)
    public String loginForm(User user, Model model) {
        LOG.info("Got user: {}");
        String userid = userDao.loggingIn(user.getLogin(), user.getPassword());
        if(checkService.checkIdForErrors(userid)){
            model.addAttribute("userid", userid);
            return "redirect:/id={userid}/showprofile";
        }
        else{
            return "redirect:/id=null/error_Incorrect login or password";
        }
    }

    @RequestMapping("/id={id}/showprofile")
    public String showProfileByID(@PathVariable String id, Model model){
        User user = userDao.findUserById(Integer.parseInt(id));
        LOG.info("Finded user{} ({})",user,userDao.userToString(user));
        model.addAttribute(user);
        return "profile";
    }

    @RequestMapping(path = "/id={id}/profile", method = RequestMethod.GET)
    public String showProfile(Model model, @PathVariable String id) {
        model.addAttribute(new User());
        if (checkService.checkIdForErrors(id)){
            model.addAttribute("id", id);
            return "redirect:/id={id}/showprofile";
        }
        else{
            return "redirect:/id={id}/login";
        }
    }

    @RequestMapping(path = "/id={id}/registration", method = RequestMethod.GET)
    public String startRegistration(Model model, @PathVariable String id) {
        if(!checkService.checkIdForErrors(id)){
            model.addAttribute(new User());
            return "registration";
        }
        else
        {
            return "redirect:/id={id}/profile";
        }
    }

    @RequestMapping(path = "/id=null/registration", method = RequestMethod.POST)
    public String finishRegistration(User user, Model model) {
        int count = userDao.countUserInDB();
        LOG.info("Got user: {}", user);
        user.setId(count);
        if (!checkService.checkUserForErrors(user, true)){
            return "redirect:/id=null/error_This login is exist";
        }
        userDao.saveUser(user);
        model.addAttribute("id", user.getId());
        userDao.loggingIn(user.getLogin(), user.getPassword());
        LOG.info("User {} logged in", user);
        return "redirect:/id={id}/showprofile";
    }


    @RequestMapping(path = "/id={id}/logout", method = RequestMethod.GET)
    public String logoutFromSite() {
        return "redirect:/index.html";
    }

    @RequestMapping(path = "/id={id}/friendlist", method = RequestMethod.GET)
    public String getFriendLists(Model model, @PathVariable String id) {
        model.addAttribute(new User());
        if (checkService.checkIdForErrors(id)){
            User currentUser = userDao.findUserById(Integer.parseInt(id));
            List<User> friendList = userDao.loadFriendListFromBD(Integer.parseInt(id));
            List<User> userList = userDao.getUserListFromNetwork();
            model.addAttribute("friendList", friendList);
            model.addAttribute("userList", userList);
            model.addAttribute("id", "id=");
            model.addAttribute("space", " ");
            userDao.countFriendsInDB();
            return "friendlist";
        }
        else{
            return "redirect:/id={id}/login";
        }
    }

    @RequestMapping(path = "/id={ab}/friendlist", method = RequestMethod.POST)
    public String addFriend(User user, Model model, @PathVariable String ab) {
        if(checkService.checkUserForErrors(userDao.findUserById(user.getId()),false)){
            LOG.info("Finded user: {}", userDao.findUserById(user.getId()).toString());
            userDao.saveFriendToFriendlistBD(Integer.parseInt(ab),user.getId());
            LOG.info("User {} added to friend",userDao.findUserById(user.getId()));
            return "redirect:/id={ab}/friendlist";
        }
        else{
            return "redirect:/id={ab}/error_User is not exist";
        }

    }

    @RequestMapping(path = "/id={id}/finduser", method = RequestMethod.GET)
    public String findUserInNetwork(Model model) {
        model.addAttribute(new User());
        return "finduser";
    }

    @RequestMapping(path = "/id={oldid}/finduser", method = RequestMethod.POST)
    public String startFinding(User user, Model model, @PathVariable String oldid) {
        User check = userDao.findUserById(user.getId());
        if(checkService.checkUserForErrors(check,false)){
            LOG.info("Finded user: {}", check.toString());
            model.addAttribute("id", user.getId());
            return "redirect:/id={oldid}/findedprofile:{id}";
        }
        else{
            return "redirect:/id={oldid}/error_User is not exist";
        }
    }
    @RequestMapping(path = "/id={id}/error_{errormessage}", method = RequestMethod.GET)
    public String errorPage(@PathVariable String errormessage, Model model){
        LOG.info("Receive error: {}", errormessage);
        model.addAttribute(errormessage);
        return "error";
    }

    @RequestMapping("/id={id}/findedprofile:{userid}")
    public String showFindedProfile(@PathVariable String userid, Model model){
        User user = userDao.findUserById(Integer.parseInt(userid));
        LOG.info("Finded user{} ({})",user,userDao.userToString(user));
        model.addAttribute(user);
        return "userprofile";
    }
}
