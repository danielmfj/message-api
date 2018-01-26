package com.jorged.messageapi.service;

import com.google.gson.Gson;
import com.jorged.messageapi.configuration.SecurityTestConfiguration;
import com.jorged.messageapi.exception.InexistentMessageException;
import com.jorged.messageapi.exception.UnauthorizedAccessException;
import com.jorged.messageapi.exception.WrongMessageFormatException;
import com.jorged.messageapi.model.Message;
import com.jorged.messageapi.model.Message.MessageBuilder;
import com.jorged.messageapi.model.MessageBoard;
import com.jorged.messageapi.service.implementation.MessageServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SecurityTestConfiguration.class)
public class MessageServiceTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    private MessageService messageService;

    @Before
    public void setUp() {

        messageService = new MessageServiceImpl();

        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();

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
    public void testAddMessageEndpoint() throws Exception {

        Message message = Message.builder()
            .userId("test1@test.com")
            .message("test")
            .build();

        Gson gson = new Gson();
        String messageRq = gson.toJson(message, Message.class);

        mockMvc.perform(post("/message/add")
            .with(user("test1@test.com").password("Test12345")).content(messageRq).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

    }

    @Test
    public void testEditMessageEndpoint() throws Exception {

        Message message = Message.builder()
            .id(1)
            .userId("test1@test.com")
            .message("test message")
            .build();

        Gson gson = new Gson();
        String messageRq = gson.toJson(message, Message.class);

        mockMvc.perform(post("/message/edit")
            .with(user("test1@test.com")
                .password("Test12345"))
            .content(messageRq)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

    }

    @Test
    public void testRemoveMessageEndpoint() throws Exception {

        mockMvc.perform(post("/message/remove/1")
            .with(user("test1@test.com")
                .password("Test12345")))
            .andExpect(status().isOk());

    }

    @Test
    public void testGetAllMessagesEndpoint() throws Exception {

        mockMvc.perform(get("/messages")
            .with(user("test1@test.com")
                .password("Test12345")))
            .andExpect(status().isOk());

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void addMessage() throws WrongMessageFormatException, UnauthorizedAccessException {

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
    public void getMessage() {

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

    @Test(expected = UnauthorizedAccessException.class)
    @WithUserDetails(value = "test1@test.com")
    public void editMessageFromOtherAuthor() throws UnauthorizedAccessException, InexistentMessageException {

        Message someonesElsesMessage = messageService.getMessagesByUser("test0@test.com").entrySet().stream().findFirst().get().getValue();
        messageService.editMessage(someonesElsesMessage);

    }

    @Test(expected = InexistentMessageException.class)
    @WithUserDetails(value = "test1@test.com")
    public void tryToEditInexistentMessage() throws InexistentMessageException, UnauthorizedAccessException {

        Message message = messageService.getMessagesByUser("test1@test.com").entrySet().stream().findFirst().get().getValue();
        message.setId(-1);
        messageService.editMessage(message);

    }

    @Test(expected = WrongMessageFormatException.class)
    public void addMessageWithWrongFormat() throws WrongMessageFormatException, UnauthorizedAccessException {

        Message message = Message.builder().build();
        messageService.addMessage(message);

    }

    @Test(expected = UnauthorizedAccessException.class)
    public void accessMessageBoardWithoutCredentials() throws WrongMessageFormatException, UnauthorizedAccessException {

        Message message = Message.builder()
                .userId("test1@test.com")
                .message("test 123")
                .timestamp(Instant.now())
                .build();

        messageService.addMessage(message);

    }

}