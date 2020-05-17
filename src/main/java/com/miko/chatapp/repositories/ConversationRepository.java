package com.miko.chatapp.repositories;

import com.miko.chatapp.models.Conversation;
import com.miko.chatapp.models.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ConversationRepository extends ReactiveMongoRepository<Conversation, String> {

    Flux<Conversation> findAllByUserOne(User user);
    Flux<Conversation> findAllByUserTwo(User user);
}
