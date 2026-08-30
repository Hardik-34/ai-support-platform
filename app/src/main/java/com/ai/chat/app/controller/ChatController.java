package com.ai.chat.app.controller;

import com.ai.chat.app.dto.ChatRequest;
import com.ai.chat.app.dto.ChatResponseDto;
import com.ai.chat.app.dto.CreateConversationRequest;
import com.ai.chat.app.model.Conversation;
import com.ai.chat.app.model.Message;
import com.ai.chat.app.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ChatController {
    private final ChatService chatService;
    public ChatController(ChatService chatService){
        this.chatService=chatService;
    }
    @PostMapping("/chat")
    public ChatResponseDto getMessage(@Valid @RequestBody ChatRequest chatRequest){
        return chatService.processMessage(chatRequest);
    }
    @PostMapping("/conversations")
        public Conversation createConversation(@Valid @RequestBody CreateConversationRequest createConversationRequest ){
        return chatService.createConversation(createConversationRequest);
    }
}
