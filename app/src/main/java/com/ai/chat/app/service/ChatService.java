package com.ai.chat.app.service;

import com.ai.chat.app.dto.ChatRequest;
import com.ai.chat.app.dto.ChatResponseDto;
import com.ai.chat.app.dto.CreateConversationRequest;
import com.ai.chat.app.model.Conversation;
import com.ai.chat.app.model.Message;
import com.ai.chat.app.repository.ConversationRepository;
import com.ai.chat.app.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.ai.chat.model.ChatModel;



@Service
public class ChatService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final AiResponseService aiResponseService;
    private final MessageService messageService;

    public ChatService(ConversationRepository conversationRepository, MessageRepository messageRepository, ChatModel chatmodel, AiResponseService aiResponseService, MessageService messageService){
        this.conversationRepository=conversationRepository;
        this.messageRepository=messageRepository;
        this.messageService = messageService;
        this.aiResponseService=aiResponseService;
    }
    public ChatResponseDto processMessage(ChatRequest chatRequest) {

        Message message=messageService.saveUserMessage(chatRequest);
        return aiResponseService.generateResponse(message.getConversation());

    }

    public Conversation createConversation(
            CreateConversationRequest request
    ) {
        Conversation conversation =
                new Conversation(request.getRequestType());

        return conversationRepository.save(conversation);
    }
}
