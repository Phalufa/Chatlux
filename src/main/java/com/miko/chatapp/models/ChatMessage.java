package com.miko.chatapp.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@NoArgsConstructor
@EqualsAndHashCode
@Document(collection = "messages")
public class ChatMessage {

    private User sender;
    private User recipient;
    private String body;
    private String conversationId;
}
