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

        createTestUsers();
    }

    private void createTestUsers() {
        User user = new User();
        user.setFirstName("Tester");
        user.setLastName("McTest");
        user.setEmail("test@test.com");
        user.setPassword("Test12345");
        user.setValidatePassword(user.getPassword());

        userList.add(user);

        user.setFirstName("Testy");
        user.setLastName("McTesty");
        user.setEmail("testy@test.com");

        userList.add(user);

        user.setFirstName("Test1");
        user.setLastName("McTest1");
        user.setEmail("test1@test.com");

        userList.add(user);
    }

}
