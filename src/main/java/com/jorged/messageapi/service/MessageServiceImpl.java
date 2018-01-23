package com.jorged.messageapi.service;

import com.jorged.messageapi.model.Message;
import com.jorged.messageapi.model.MessageBoard;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    private List<Message> messageBoard = MessageBoard.getInstance().getMessageList();

    @Override
    public Boolean addMessage(Message message) {

        if (message.getId() != null) {
            return false;
        }

        message.setId(messageBoard.size());
        messageBoard.add(message);

        return true;
    }

    @Override
    public List<Message> getMessages() {
        return messageBoard;
    }

    @Override
    public Message getMessage(Integer messageId) {
        return messageBoard.get(messageId);
    }

    @Override
    public List<Message> getMessagesByUser(String userId) {
        return messageBoard.stream()
                .filter(message -> userId.contentEquals(message.getUserId()))
                .collect(Collectors.toList());
    }

    @Override
    public Message editMessage(Message message) {
        return null;
    }

    @Override
    public void removeMessageById(Integer messageId) {

    }
}
