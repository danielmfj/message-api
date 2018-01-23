package com.jorged.messageapi.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class MessageBoard {

    @Getter
    private static MessageBoard instance = new MessageBoard();

    @Getter
    private List<User> userList;

    @Getter
    private List<Message> messageList;

    private MessageBoard() {
        userList = new ArrayList<>();
        messageList = new ArrayList<>();
    }

}
