package web;

import dao.MessageDao;
import dao.UserDao;
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
import services.impl.CheckServiceImpl;

@Controller
public class HomeController {
    private static Logger LOG = LogManager.getLogger();
    private final UserDao userDao;
    private final MessageDao messageDao;
    private CheckService checkService;


    @Autowired
    public HomeController(UserDao userDao, MessageDao messageDao) {
        this.userDao = userDao;
        this.messageDao = messageDao;
        this.checkService = new CheckServiceImpl(userDao, messageDao);
    }


    @RequestMapping(path = {"/","/index"}, method = RequestMethod.GET)
    public String home(Model model) {
        LOG.info("Home page without login, adding NULL");
        return "redirect:/id=null/index";
    }

    @RequestMapping(path = {"/id={id}","/id={id}/index"}, method = RequestMethod.GET)
    public String homeWithID(@PathVariable String id) {
        if(StringUtils.equals("null",id)){
            LOG.info("Page index, id = null");
            return "index";
        }
        else{
            if(checkService.checkIdForErrors(id)){
                LOG.info("Page index, id = {}", Integer.parseInt(id));
                return "index";
            }
            else{
                LOG.info("Page index, errorID, redirect to id=null");
                return "redirect:/id=null";
            }
        }
    }



}