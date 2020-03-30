package de.hfu;

import de.hfu.messages.domain.model.Message;
import de.hfu.messages.domain.model.User;
import de.hfu.messages.domain.service.MessageService;
import de.hfu.messages.domain.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {

    private MessageService messageService;
    private SecurityService securityService;

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @RequestMapping("/messages.html")
    public ModelAndView allMessages() {
        ModelAndView mav = new ModelAndView();
        List<Message> messages = messageService.findAllMessages();
        mav.addObject("messages", messages);
        mav.addObject("lastClientMessage",
                (messages.size() == 0)? 0 : messages.get(0).getDate().getTime());
        mav.setViewName("messages");
        return mav;
    }

    @RequestMapping("/messageForm.html")
    public String messageForm() {
        return "messageForm";
    }

    @RequestMapping("/createMessage.html")
    public String createMessage(@RequestParam("chatInput") String text,
                                Principal principal) {
        System.out.println("user " + principal.getName() + " created message:" + text);
        User user = messageService.findUserByUsername(principal.getName());
        Date date = new Date();
        date.setTime(date.getTime() - 5000);
        Message message = new Message(text, date, user);
        messageService.saveMessage(message);
        return "messages";
    }

    @RequestMapping("/login.html")
    public String login() {
        return "login";
    }

    @RequestMapping(value="/register.html") public ModelAndView registerInput() {
        ModelAndView mav = new ModelAndView(); mav.setViewName("register"); return mav;
    }

    @RequestMapping(value="/registerSave.html")
    public ModelAndView registerSave(String firstname, String lastname, String email, String password) {
        ModelAndView mav = new ModelAndView();
        String fullname = firstname + " " + lastname;
        System.out.println("registering user " + fullname);
        User registerUser = new User(fullname, securityService.encodePassword(password), fullname, email);
        try {
            messageService.createUser(registerUser);
            mav.setViewName("registerSuccess");
        }
        catch (Exception exception) {
            mav.addObject("fehler", exception.getMessage());
            mav.setViewName("register");
            System.out.println("cannot create user " + fullname + " : " + exception.getMessage());
        }
        return mav;
    }


    @Autowired
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService; }

    @RequestMapping("/users.html")
    public ModelAndView allUsers() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("users", messageService.findAllUsers());
        mav.setViewName("users");
        return mav;
    }


    @RequestMapping("/ajax/messages.json")
    @ResponseBody
    public List<Message> messages(@RequestParam(required = false) Long lastClientMessage) {
        if (lastClientMessage == null) { lastClientMessage = 0L;
        }
        return messageService.findLatestMessages(new Date(lastClientMessage)); }

}

