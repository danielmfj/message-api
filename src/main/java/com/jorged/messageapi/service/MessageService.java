package com.jorged.messageapi.service;

import com.jorged.messageapi.model.Message;

import java.util.List;

public interface MessageService {

    Boolean addMessage(Message message);
    List<Message> getMessages();
    Message getMessage(Integer messageId);
    List<Message> getMessagesByUser(String userId);
    Message editMessage(Message message);
    void removeMessageById(Integer messageId);

}
