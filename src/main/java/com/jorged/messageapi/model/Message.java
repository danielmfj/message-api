package com.jorged.messageapi.model;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Builder
@Data
public class Message {

    Integer id;

    @NotNull
    @NotEmpty
    String userId;

    Instant timestamp;

    @NotNull
    @NotEmpty
    String message;

    public static Boolean isValid(Message message) {
        return StringUtils.isNotEmpty(message.getUserId()) && StringUtils.isNotEmpty(message.getMessage());
    }

}
