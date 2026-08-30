package com.ai.chat.app.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Getter
@Setter
public class ChatRequest {
    @NotNull
    @Id
    private UUID conversationId;

    @NotBlank
    @Size(max = 2000)
    private String message;

}
