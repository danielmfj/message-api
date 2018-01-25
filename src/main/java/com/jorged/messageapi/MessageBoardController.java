package com.jorged.messageapi;

import com.jorged.messageapi.exception.ExistentUserException;
import com.jorged.messageapi.exception.InexistentMessageException;
import com.jorged.messageapi.exception.UnauthorizedAccessException;
import com.jorged.messageapi.exception.WrongMessageFormatException;
import com.jorged.messageapi.model.Message;
import com.jorged.messageapi.model.User;
import com.jorged.messageapi.service.MessageService;
import com.jorged.messageapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class MessageBoardController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/message/add", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE}, method = RequestMethod.POST)
    public Boolean addMessage(@RequestBody @Valid Message message) throws WrongMessageFormatException {
        return messageService.addMessage(message);
    }

    @RequestMapping(value = "/message/edit", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE}, method = RequestMethod.POST)
    public Message editMessage(@RequestBody @Valid Message message) throws UnauthorizedAccessException, InexistentMessageException {
        return messageService.editMessage(message);
    }

    @RequestMapping(value = "/message/remove/{messageId}", method = RequestMethod.POST)
    public void removeMessage(@PathVariable Integer messageId) throws UnauthorizedAccessException, InexistentMessageException {
        messageService.removeMessageById(messageId);
    }

    @RequestMapping(value = "/user/{userId}/messages", method = RequestMethod.GET)
    public Map<Integer, Message> retrieveMessagesForUser(@PathVariable String userId) throws UnauthorizedAccessException {
        return messageService.getMessagesByUser(userId);
    }

    @RequestMapping("/signup")
    public ModelAndView signUpAction(Model model) {

        User user = new User();
        return new ModelAndView("/signup", "user", user);

    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView loginAction(@ModelAttribute("user") User user, BindingResult result, WebRequest request, Errors errors) throws UnauthorizedAccessException {

        return login(user);

    }

    @RequestMapping("/logout")
    public ModelAndView logoutAction(Model model) {

        return new ModelAndView("/");

    }

    @RequestMapping("/panel")
    public ModelAndView panelAction(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.getUser(auth.getName());
        Map<Integer, Message> messages = messageService.getMessages();
        Message message = Message.builder().message("").build();

        Map<String, Object> content = new HashMap<>();
        content.put("user", user);
        content.put("messages", messages);
        content.put("message", message);

        return new ModelAndView("/panel", "content", content);

    }

    @RequestMapping(value = "/user/signup", method = RequestMethod.POST)
    public ModelAndView signUpUser(@ModelAttribute("user") @Valid User user, BindingResult result, WebRequest request, Errors errors) throws UnauthorizedAccessException {

        User registered = new User();

        if (!result.hasErrors()) {
            registered = createUser(user);
        }

        if (registered == null) {
            result.rejectValue("email", "message.regError");
        }

        if (result.hasErrors()) {
            return new ModelAndView("/signup", "user", user);
        } else {
            return login(user);
        }

    }

    private User createUser(User user) {

        User newUser;

        try {
            newUser = userService.registerUser(user);
        } catch (ExistentUserException e) {
            return null;
        }

        return newUser;
    }

    private ModelAndView login(User user) throws UnauthorizedAccessException {
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            throw new UnauthorizedAccessException("Not authorized");
        }

        return new ModelAndView("redirect:/panel", "user", user);
    }

}
