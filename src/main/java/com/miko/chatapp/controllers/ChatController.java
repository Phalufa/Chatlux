package com.miko.chatapp.controllers;

import com.miko.chatapp.models.Conversation;
import com.miko.chatapp.models.ChatMessage;
import com.miko.chatapp.models.User;
import com.miko.chatapp.repositories.ConversationRepository;
import com.miko.chatapp.repositories.ChatMessageRepository;
import com.miko.chatapp.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * Includes all the application chat actions
 *
 */
@RestController
@RequestMapping("/app/chat")
@AllArgsConstructor
public class ChatController {

    private final ChatMessageRepository messageRepo;
    private final ConversationRepository conversationRepo;
    private final UserRepository userRepo;

    /**
     * Creates a new conversation in order to user { sendMessage() }.
     *
     * @param conversation the chat conversation between users
     * @return the chat conversation object after save
     */
    @PostMapping
    public Mono<Conversation> createConversation(@RequestBody final Conversation conversation){
        return conversationRepo.save(conversation);
    }

    /**
     * Gets all the conversations of the application
     *
     * @return all the conversations
     */
    @GetMapping("/all")
    public Flux<Conversation> getConversations() {
        return conversationRepo.findAll();
    }

    /**
     * Gets all of the conversations of a user by
     *
     * @param userId the user's ID
     * @return the user's conversations
     */
    @GetMapping("/conversations/{userId}")
    public Flux<Conversation> getUserConversations(@PathVariable String userId) {
        Mono<User> userMono = userRepo.findById(userId);
        return userMono.flatMapMany(conversationRepo::findAllByUserOne)
                .concatWith(userMono.flatMapMany(conversationRepo::findAllByUserTwo));
    }

    /**
     * Creates a ChatMessage to be sent via { streamMessagesOfConversation() }.
     *
     * @param message the message to send
     * @return the message object after insertion
     */
    @PostMapping("/send")
    public Mono<ChatMessage> sendMessage(@RequestBody final ChatMessage message) {
        return messageRepo.insert(message);
    }

    /**
     * Gets a continuous stream of messages from a conversation.
     *
     * @param conversationId the conversation ID to get the messages from
     * @return a stream of the conversation's messages
     */
    @GetMapping(value = "/{conversationId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatMessage> streamMessagesOfConversation(@PathVariable final String conversationId) {
        return messageRepo.findChatMessageByConversationId(conversationId)
                .delayElements(Duration.ofMillis(100))
                .log();
    }
}
