package com.jorged.messageapi.service;

import com.jorged.messageapi.configuration.SecurityTestConfiguration;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SecurityTestConfiguration.class)
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
                    .id(i)
                    .userId("test" + i % 2 + "@test.com")
                    .message("test " + i)
                    .timestamp(instant);

            MessageBoard.getInstance().getMessages().put(i, messageBuilder.build());

        }

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void addMessage() {
        Message message = Message.builder()
                .userId("test1@test.com")
                .message("test 123")
                .timestamp(Instant.now())
                .build();

        assertTrue(messageService.addMessage(message));
    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void getMessages() {
        Map<Integer, Message> messageList = messageService.getMessages();
        assertEquals(messageList, MessageBoard.getInstance().getMessages());
    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void getMessage() throws InexistentMessageException {
        Message message = messageService.getMessage(4);
        assertEquals(message, MessageBoard.getInstance().getMessages().get(4));
    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void getMessagesByUser() {
        Map<Integer, Message> messagesByUserFromService = messageService.getMessagesByUser("test1@test.com");
        Map<Integer, Message> messagesByUserFromMock = MessageBoard.getInstance().getMessages().entrySet()
                .stream()
                .filter(message -> "test1@test.com".contentEquals(message.getValue().getUserId()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        assertEquals(messagesByUserFromService, messagesByUserFromMock);
    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void editMessage() throws UnauthorizedAccessException, InexistentMessageException {
        Message message = Message.builder()
                .id(1)
                .userId("test1@test.com")
                .message("test 123")
                .timestamp(Instant.now())
                .build();

        Message messageFromService = messageService.editMessage(message);

        assertEquals(messageFromService, message);
    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void removeMessageById() throws InexistentMessageException, UnauthorizedAccessException {

        Boolean existsBefore = messageService.getMessage(1) != null;

        messageService.removeMessageById(1);

        Boolean existsAfter = messageService.getMessage(1) != null;

        assertNotEquals(existsBefore, existsAfter);
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