package com.jorged.messageapi.model;

import com.jorged.messageapi.annotation.PasswordsMatch;
import com.jorged.messageapi.annotation.ValidEmail;
import com.jorged.messageapi.annotation.ValidPassword;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

@Data
@PasswordsMatch
public class User {

    private static final String ROLE_USER = "ROLE_USER";

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

    private List<String> roles = Collections.singletonList(ROLE_USER);
    private List<GrantedAuthority> authorities = Collections.singletonList((GrantedAuthority) () -> ROLE_USER);

}
