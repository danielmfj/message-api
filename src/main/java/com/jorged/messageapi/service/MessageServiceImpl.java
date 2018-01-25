package com.jorged.messageapi.service;

import com.jorged.messageapi.exception.InexistentMessageException;
import com.jorged.messageapi.exception.UnauthorizedAccessException;
import com.jorged.messageapi.model.Message;
import com.jorged.messageapi.model.MessageBoard;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    private Map<Integer, Message> messageBoard = MessageBoard.getInstance().getMessages();
    private AtomicInteger messageIdGenerator = MessageBoard.getInstance().getMessageIdGenerator();

    @Override
    public Boolean addMessage(Message message) {

        if (message.getId() != null) {
            return false;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer messageId = messageIdGenerator.getAndIncrement();

        message.setId(messageId);
        message.setTimestamp(Instant.now());
        message.setUserId(auth.getName());
        messageBoard.put(messageId, message);

        return true;
    }

    @Override
    public Map<Integer, Message> getMessages() {
        return messageBoard;
    }

    @Override
    public Message getMessage(Integer messageId) {
        return messageBoard.get(messageId);
    }

    @Override
    public Map<Integer, Message> getMessagesByUser(String userId) {
        return messageBoard.entrySet().stream()
                .filter(message -> userId.contentEquals(message.getValue().getUserId()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Message editMessage(Message message) throws UnauthorizedAccessException, InexistentMessageException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.getName().contentEquals(message.getUserId())) {
            throw new UnauthorizedAccessException("No access to resource.");
        }

        if (!messageBoard.entrySet().stream().anyMatch(m -> m.getKey().equals(message.getId()))) {
            throw new InexistentMessageException("Message doesn't exist");
        }

        message.setTimestamp(Instant.now());
        message.setUserId(auth.getName());

        messageBoard.put(message.getId(), message);

        return message;
    }

    @Override
    public void removeMessageById(Integer messageId) {
        messageBoard.remove(messageId);
    }
}
