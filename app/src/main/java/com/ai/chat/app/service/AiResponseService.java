package com.ai.chat.app.service;

import com.ai.chat.app.dto.ChatResponseDto;
import com.ai.chat.app.model.Conversation;
import com.ai.chat.app.model.MessageSender;
import com.ai.chat.app.repository.MessageRepository;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AiResponseService {

    private final ChatModel chatModel;
    private final MessageRepository messageRepository;

    public AiResponseService(ChatModel chatModel, MessageRepository messageRepository) {
        this.chatModel = chatModel;
        this.messageRepository = messageRepository;
    }

    public ChatResponseDto generateResponse(Conversation conversation){

        // Get conversation history
        List<com.ai.chat.app.model.Message> conversationMessages =
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
        com.ai.chat.app.model.Message aiMessage = new com.ai.chat.app.model.Message();
        aiMessage.setConversation(conversation);
        aiMessage.setSender(MessageSender.AI);
        aiMessage.setContent(aiContent);
        aiMessage.setCreatedAt(LocalDateTime.now());

        com.ai.chat.app.model.Message savedAiMessage = messageRepository.save(aiMessage);

        // Return API DTO
        return new ChatResponseDto(
                conversation.getId(),
                savedAiMessage.getContent(),
                savedAiMessage.getSender(),
                savedAiMessage.getCreatedAt()
        );
    }
    private org.springframework.ai.chat.messages.Message toAiMessage(
            com.ai.chat.app.model.Message message) {

        return switch (message.getSender()) {
            case USER -> new UserMessage(message.getContent());
            case AI -> new AssistantMessage(message.getContent());
        };
    }
}
