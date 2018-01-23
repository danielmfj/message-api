package com.jorged.messageapi.model;

import com.jorged.messageapi.validation.PasswordsMatch;
import com.jorged.messageapi.validation.ValidEmail;
import com.jorged.messageapi.validation.ValidPassword;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@Data
@PasswordsMatch
public class User {

    @NotNull
    @NotEmpty
    @ValidEmail
    private String email;

    @NotNull
    @NotEmpty
    @ValidPassword
    private String password;

    private String validatePassword;

    @NotNull
    @NotEmpty
    private String firstName;

    @NotNull
    @NotEmpty
    private String lastName;

    private List<String> roles = Arrays.asList("ROLE_USER");

}
