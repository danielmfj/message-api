package com.jorged.messageapi.service;

import com.jorged.messageapi.exception.InexistentMessageException;
import com.jorged.messageapi.exception.UnauthorizedAccessException;
import com.jorged.messageapi.exception.WrongMessageFormatException;
import com.jorged.messageapi.model.Message;
import com.jorged.messageapi.model.Message.MessageBuilder;
import com.jorged.messageapi.model.MessageBoard;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class MessageServiceTest {

    private MessageService messageService = new MessageServiceImpl();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {

        Instant instant = Instant.now();

        MessageBuilder messageBuilder;

        for (int i = 1; i <= 10; i++) {

            instant = instant.plusSeconds(new Random().nextInt(i));

            messageBuilder = Message.builder()
                    .userId("test" + i % 2 + "@test.com")
                    .message("test " + i)
                    .timestamp(instant);

            MessageBoard.getInstance().getMessageList().add(messageBuilder.build());

        }

    }

    @Test
    public void addMessage() {
        Message message = Message.builder()
                .userId("test1@test.com")
                .message("test 123")
                .timestamp(Instant.now())
                .build();

        assertTrue(messageService.addMessage(message));
    }

    @Test
    public void getMessages() {
        List<Message> messageList = messageService.getMessages();
        assertEquals(messageList, MessageBoard.getInstance().getMessageList());
    }

    @Test
    public void getMessage() {
        Message message = messageService.getMessage(0);
        assertEquals(message, MessageBoard.getInstance().getMessageList().get(0));
    }

    @Test
    public void getMessagesByUser() {
        List<Message> messagesByUserFromService = messageService.getMessagesByUser("test1@test.com");
        List<Message> messagesByUserFromMock = MessageBoard.getInstance().getMessageList()
                .stream()
                .filter(message -> "test1@test.com".contentEquals(message.getUserId()))
                .collect(Collectors.toList());

        assertEquals(messagesByUserFromService, messagesByUserFromMock);
    }

    @Test
    @Ignore
    public void editMessage() {
        Message message = Message.builder()
                .id(0)
                .userId("test1@test.com")
                .message("test 123")
                .timestamp(Instant.now())
                .build();

        Message messageFromService = messageService.editMessage(message);

        assertEquals(messageFromService, message);

    }

    @Test
    public void removeMessageById() {

    }

    @Test
    @Ignore
    public void editMessageFromOtherAuthor() throws UnauthorizedAccessException {
        expectedException.expect(UnauthorizedAccessException.class);
    }

    @Test
    @Ignore
    public void tryToRetrieveInexistentMessage() throws InexistentMessageException {
        expectedException.expect(InexistentMessageException.class);
    }

    @Test
    @Ignore
    public void addMessageWithWrongFormat() throws WrongMessageFormatException {
        expectedException.expect(WrongMessageFormatException.class);
    }

}