package com.miko.chatapp.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document(collection = "conversations")
public class Conversation {

    @Id
    @EqualsAndHashCode.Include
    private String id;
    @Setter
    private User userOne;
    @Setter
    private User userTwo;
}
