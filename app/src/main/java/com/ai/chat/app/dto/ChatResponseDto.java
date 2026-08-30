package com.ai.chat.app.dto;

import com.ai.chat.app.model.MessageSender;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ChatResponseDto {

    private UUID conversationId;
    private String message;
    private MessageSender sender;
    private LocalDateTime createdAt;
}