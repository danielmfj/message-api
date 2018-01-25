package com.jorged.messageapi.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageBoard {

    @Getter
    private static MessageBoard instance = new MessageBoard();

    @Getter
    private List<User> userList;

    @Getter
    private Map<Integer, Message> messages;

    @Getter
    private AtomicInteger messageIdGenerator;

    private MessageBoard() {
        userList = new ArrayList<>();
        messages = new HashMap<>();
        messageIdGenerator = new AtomicInteger();
    }

}
