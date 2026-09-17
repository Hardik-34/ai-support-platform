package com.ai.chat.app.service;

import com.ai.chat.app.dto.ChatRequest;
import com.ai.chat.app.exception.ConversationNotFoundException;
import com.ai.chat.app.model.Conversation;
import com.ai.chat.app.model.Message;
import com.ai.chat.app.model.MessageSender;
import com.ai.chat.app.repository.ConversationRepository;
import com.ai.chat.app.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class MessageService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    public MessageService(
            ConversationRepository conversationRepository,
            MessageRepository messageRepository
    ) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
    }

    @Transactional
    public Message saveUserMessage(ChatRequest chatRequest){

        Conversation conversation = conversationRepository
                .findById(chatRequest.getConversationId())
                .orElseThrow(() ->
                        new ConversationNotFoundException("Conversation not found"));

        Message userMessage = new Message();
        userMessage.setConversation(conversation);
        userMessage.setContent(chatRequest.getMessage());
        userMessage.setCreatedAt(LocalDateTime.now());
        userMessage.setSender(MessageSender.USER);

        return messageRepository.save(userMessage);
    }
}
