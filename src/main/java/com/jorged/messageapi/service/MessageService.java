package com.jorged.messageapi.service;

import com.jorged.messageapi.exception.InexistentMessageException;
import com.jorged.messageapi.exception.UnauthorizedAccessException;
import com.jorged.messageapi.exception.WrongMessageFormatException;
import com.jorged.messageapi.model.Message;

import java.util.List;
import java.util.Map;

public interface MessageService {

    Boolean addMessage(Message message) throws WrongMessageFormatException, UnauthorizedAccessException;
    Map<Integer, Message> getMessages();
    Message getMessage(Integer messageId);
    Map<Integer, Message> getMessagesByUser(String userId);
    Message editMessage(Message message) throws UnauthorizedAccessException, InexistentMessageException;
    void removeMessageById(Integer messageId) throws UnauthorizedAccessException, InexistentMessageException;

}
