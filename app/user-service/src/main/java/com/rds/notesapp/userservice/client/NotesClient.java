package com.rds.notesapp.userservice.client;

import com.rds.notesapp.userservice.dto.Note;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@FeignClient(name = "notes-service")
public interface NotesClient {

    @GetMapping("/api/notes/user/{userId}")
    @CircuitBreaker(name = "notesClient", fallbackMethod = "getNotesByUserIdFallback")
    List<Note> getNotesByUserId(@PathVariable("userId") long userId);

    default List<Note> getNotesByUserIdFallback(Exception e) {
        System.out.println("Fallback method called for getNotesByUserId with userId: " + e.getMessage());
        return List.of();
    }
}