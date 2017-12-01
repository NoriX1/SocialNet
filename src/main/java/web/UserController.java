package web;

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

import java.util.List;

@Controller
@RequestMapping("/")
public class UserController {
    private static Logger LOG = LogManager.getLogger();
    private final UserDao userDao;

    @Autowired
    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @RequestMapping("/id={id}")
    public String profilebyid(@PathVariable int id, Model model){
        User user = userDao.findUserById(id);
        LOG.info("Finded user{} ({})",user,userDao.userToString(user));
        model.addAttribute(user);
        return "profile";
    }

    @RequestMapping(path = "/profile", method = RequestMethod.GET)
    public String showprofile(Model model) {
        model.addAttribute(new User());
        if (userDao.isUserLogged()){
            int id = userDao.getCurrentUserID();
            model.addAttribute("id", id);
            return "redirect:/id={id}";
        }
        else{
            return "redirect:/login";
        }
    }

    @RequestMapping(path = "/registration", method = RequestMethod.GET)
    public String startRegistration(Model model) {
        model.addAttribute(new User());
        return "registration";
    }

    @RequestMapping(path = "/registration", method = RequestMethod.POST)
    public String finishRegistration(User user, Model model) {
        int count = userDao.countUserInDB();
        LOG.info("Got user: {}", user);
        user.setId(count);
        if (!userDao.checkUserForErrors(user, true)){
            return "redirect:/error_This login is exist";
        }
        userDao.saveUser(user);
        model.addAttribute("id", user.getId());
        userDao.loggingIn(user.getLogin(), user.getPassword());
        LOG.info("User {} logged in", user);
        return "redirect:/id={id}";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String logintosite(Model model) {
        if(!userDao.isUserLogged()){
            model.addAttribute(new User());
            return "login";
        }
        else
            return "redirect:/profile";
    }


    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String logintositeform(User user, Model model) {
        LOG.info("Got user: {}", user);
        if(!userDao.loggingIn(user.getLogin(), user.getPassword())){
            return "redirect:/error_Incorrect login or password";
        }
        else{
            model.addAttribute("id", userDao.getCurrentUserID());
            return "redirect:/id={id}";
        }
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logoutfromsite() {
        userDao.logoutUser();
        if(!userDao.isUserLogged()){
            return "redirect:/index.html";
        }
        else{
            return "redirect:/error_You are not logged";
        }
    }

    @RequestMapping(path = "/friendlist", method = RequestMethod.GET)
    public String getlists(Model model) {
        model.addAttribute(new User());
        if (userDao.isUserLogged()){
            User currentUser = userDao.findUserById(userDao.getCurrentUserID());
            userDao.loadFriendListFromBD(currentUser);
            List<User> friendList = currentUser.getFriendlist();
            List<User> userList = userDao.getUserListFromNetwork();
            model.addAttribute("friendList", friendList);
            model.addAttribute("userList", userList);
            model.addAttribute("id", "id=");
            model.addAttribute("space", " ");
            return "friendlist";
        }
        else{
            return "redirect:/login";
        }
    }

    @RequestMapping(path = "/friendlist", method = RequestMethod.POST)
    public String addfriend(User user, Model model) {
        if(userDao.checkUserForErrors(userDao.findUserById(user.getId()),false)){
            LOG.info("Finded user: {}", userDao.findUserById(user.getId()).toString());
            userDao.saveFriendToFriendlistBD(userDao.getCurrentUserID(),user.getId());
            LOG.info("User {} added to friend",userDao.findUserById(user.getId()));
            return "redirect:/friendlist";
        }
        else{
            return "redirect:/error_User is not exist";
        }

    }

    @RequestMapping(path = "/finduser", method = RequestMethod.GET)
    public String findUserInNetwork(Model model) {
        model.addAttribute(new User());
        return "finduser";
    }

    @RequestMapping(path = "/finduser", method = RequestMethod.POST)
    public String startFinding(User user, Model model) {
        User check = userDao.findUserById(user.getId());
        if(userDao.checkUserForErrors(check,false)){
            LOG.info("Finded user: {}", check.toString());
            model.addAttribute("id", user.getId());
            return "redirect:/id={id}";
        }
        else{
            return "redirect:/error_User is not exist";
        }
    }
    @RequestMapping(path = "/error_{errormessage}", method = RequestMethod.GET)
    public String error(@PathVariable String errormessage, Model model){
        LOG.info("Receive error: {}", errormessage);
        model.addAttribute(errormessage);
        return "error";
    }
}
