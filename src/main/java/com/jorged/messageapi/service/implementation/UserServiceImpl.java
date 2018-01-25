package com.jorged.messageapi.service.implementation;

import com.jorged.messageapi.exception.ExistentUserException;
import com.jorged.messageapi.model.MessageBoard;
import com.jorged.messageapi.model.User;
import com.jorged.messageapi.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private List<User> userList = MessageBoard.getInstance().getUserList();

    @Override
    public User registerUser(User user) throws ExistentUserException{

        Boolean userExists = userList
                .stream()
                .anyMatch(u -> u.getEmail().contentEquals(user.getEmail()));

        if (userExists) {
            throw new ExistentUserException("User already registered!");
        }

        userList.add(user);

        return user;

    }

    @Override
    public User getUser(String email) {

        return userList.stream()
                .filter(user -> user.getEmail().contentEquals(email))
                .findFirst()
                .orElse(null);

    }

}
