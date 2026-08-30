package com.ai.chat.app.dto;

import com.ai.chat.app.model.RequestType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateConversationRequest {
    @NotNull
    private RequestType requestType;
}
