package com.ai.chat.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name="conversation_id",nullable = false)
    private Conversation conversation;

    @Enumerated(EnumType.STRING)
    private MessageSender sender;

    @Column(nullable = false, length = 2000)
    private String content;

    private LocalDateTime createdAt;
}
