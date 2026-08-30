package com.ai.chat.app.repository;

import com.ai.chat.app.model.Conversation;
import com.ai.chat.app.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
   List<Message> findByConversationOrderByCreatedAtAsc(Conversation conversation);
}
