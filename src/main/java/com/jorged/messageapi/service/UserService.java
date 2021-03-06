package com.jorged.messageapi.service;

import com.jorged.messageapi.exception.ExistentUserException;
import com.jorged.messageapi.model.User;

public interface UserService {

    User registerUser(User user) throws ExistentUserException;
    User getUser(String email);

}
