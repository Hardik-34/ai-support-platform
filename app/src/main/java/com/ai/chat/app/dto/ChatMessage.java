package com.ai.chat.app.dto;

import com.ai.chat.app.model.MessageSender;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class ChatMessage {
    private String content;
    private MessageSender sender;
}
