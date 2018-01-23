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
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class MessageBoardController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/message/add", method = RequestMethod.PUT)
    public Boolean addMessage(@RequestBody Message message) throws WrongMessageFormatException {
        return messageService.addMessage(message);
    }

    @RequestMapping(value = "/message/edit", method = RequestMethod.POST)
    public Message editMessage(@RequestBody Message message) throws WrongMessageFormatException {
        return messageService.editMessage(message);
    }

    @RequestMapping(value = "/message/remove/{messageId}", method = RequestMethod.DELETE)
    public void removeMessage(@PathVariable Integer messageId) throws UnauthorizedAccessException, InexistentMessageException {
        messageService.removeMessageById(messageId);
    }

    @RequestMapping(value = "/user/{userId}/messages", method = RequestMethod.GET)
    public List<Message> retrieveMessagesForUser(@PathVariable String userId) throws UnauthorizedAccessException {
        return messageService.getMessagesByUser(userId);
    }

    @RequestMapping(value = "/user/registration", method = RequestMethod.POST)
    public ModelAndView registerUser(@ModelAttribute("user") @Valid User user, BindingResult result, WebRequest request, Errors errors) {

        User registered = new User();

        if (!result.hasErrors()) {
            registered = createUser(user);
        }

        if (registered == null) {
            result.rejectValue("email", "message.regError");
        }

        if (result.hasErrors()) {
            return new ModelAndView("registration", "user", user);
        } else {
            return new ModelAndView("panel", "user", user);
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

}
