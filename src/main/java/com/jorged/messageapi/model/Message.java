package com.jorged.messageapi.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Builder
@Data
public class Message {

    Integer id;
    String userId;
    Instant timestamp;
    String message;

}
