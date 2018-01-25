package com.jorged.messageapi.validation;

import com.jorged.messageapi.annotation.PasswordsMatch;
import com.jorged.messageapi.model.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatcherValidator implements ConstraintValidator<PasswordsMatch, User> {

    @Override
    public void initialize(PasswordsMatch password) {

    }

    @Override
    public boolean isValid(User user, ConstraintValidatorContext constraintValidatorContext) {
        return user.getPassword().contentEquals(user.getValidatePassword());
    }

}
