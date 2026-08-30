package com.ai.chat.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private RequestType requestType;

    private LocalDateTime createdAt;

    public Conversation(RequestType requestType){
        this.requestType=requestType;
        this.createdAt=LocalDateTime.now();
    }


}
