package com.miko.chatapp.controllers;

import com.miko.chatapp.models.User;
import com.miko.chatapp.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Rest controller for the users of the application
 */
@RestController
@AllArgsConstructor
@RequestMapping("/app/user")
public class UserController {

    private final UserRepository userRepo;

    /**
     * Gets all users
     *
     * @return all users
     */
    @GetMapping
    public Flux<User> getAll() {
        return userRepo.findAll();
    }

    /**
     * Gets one user
     *
     * @param id the user's ID to get
     * @return the user entity
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<User>> get(@PathVariable final String id) {
        return userRepo.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Creates a user
     *
     * @param user the user to create
     * @return the user entity after creation
     */
    @PostMapping
    public Mono<ResponseEntity<User>> create(@RequestBody final User user) {
        return userRepo.save(user)
                .map(ResponseEntity::ok);
    }

    /**
     * Updates a user.
     * Status code 400 will be returned if the user is not found
     *
     * @param user the user to update
     * @return the user entity after update
     */
    @PutMapping
    public Mono<ResponseEntity<User>> update(@RequestBody final User user) {
        return userRepo.findById(user.getId())
                .flatMap(db -> {
                    db.setName(user.getName());
                    db.setEmail(user.getEmail());   // TODO response notfound in case of duplication
                    db.setPassword(user.getPassword());
                    return userRepo.save(db);
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    /**
     * Deletes a user.
     * Status code 200 will be returned after a successful deletion, 400 otherwise
     *
     * @param id the user's ID to delete
     * @return status code 200
     */
    @DeleteMapping("/del/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return userRepo.findById(id)
                .flatMap(db -> userRepo.delete(db)
                        .then(Mono.just(ResponseEntity.ok().<Void>build())))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
