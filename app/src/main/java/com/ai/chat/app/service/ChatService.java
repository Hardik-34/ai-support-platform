package com.ai.chat.app.service;

import com.ai.chat.app.dto.ChatMessage;
import com.ai.chat.app.dto.ChatRequest;
import com.ai.chat.app.dto.ChatResponseDto;
import com.ai.chat.app.dto.CreateConversationRequest;
import com.ai.chat.app.model.Conversation;
import com.ai.chat.app.model.Message;
import com.ai.chat.app.model.MessageSender;
import com.ai.chat.app.repository.ConversationRepository;
import com.ai.chat.app.repository.MessageRepository;
import jakarta.validation.Valid;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import org.springframework.ai.chat.model.ChatModel;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Service
public class ChatService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final ChatModel chatModel;

    public ChatService( ConversationRepository conversationRepository,MessageRepository messageRepository,ChatModel chatmodel){
        this.conversationRepository=conversationRepository;
        this.messageRepository=messageRepository;
        this.chatModel=chatmodel;
    }
    public ChatResponseDto processMessage(ChatRequest chatRequest) {

        UUID id = chatRequest.getConversationId();
        String content = chatRequest.getMessage();

        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        // Save USER message
        Message userMessage = new Message();
        userMessage.setConversation(conversation);
        userMessage.setContent(content);
        userMessage.setCreatedAt(LocalDateTime.now());
        userMessage.setSender(MessageSender.USER);

        messageRepository.save(userMessage);

        // Get conversation history
        List<Message> conversationMessages =
                messageRepository.findByConversationOrderByCreatedAtAsc(conversation);

        // Convert DB messages to Spring AI messages
        List<org.springframework.ai.chat.messages.Message> aiMessages =
                conversationMessages.stream()
                        .map(this::toAiMessage)
                        .toList();

        // Call AI
        Prompt prompt = new Prompt(aiMessages);
        ChatResponse aiResponse = chatModel.call(prompt);

        String aiContent = aiResponse.getResult()
                .getOutput()
                .getText();

        // Save AI message
        Message aiMessage = new Message();
        aiMessage.setConversation(conversation);
        aiMessage.setSender(MessageSender.AI);
        aiMessage.setContent(aiContent);
        aiMessage.setCreatedAt(LocalDateTime.now());

        Message savedAiMessage = messageRepository.save(aiMessage);

        // Return API DTO
        return new ChatResponseDto(
                conversation.getId(),
                savedAiMessage.getContent(),
                savedAiMessage.getSender(),
                savedAiMessage.getCreatedAt()
        );
    }

    private org.springframework.ai.chat.messages.Message toAiMessage(
            Message message) {

        return switch (message.getSender()) {
            case USER -> new UserMessage(message.getContent());
            case AI -> new AssistantMessage(message.getContent());
        };
    }

    public Conversation createConversation(CreateConversationRequest request) {
        Conversation conversation =
                new Conversation(request.getRequestType());

        return conversationRepository.save(conversation);
    }
}
