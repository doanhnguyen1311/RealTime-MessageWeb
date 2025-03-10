package com.example.identity_service.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {
    private String senderId;
    private String receiverId;
    private String content;
}
