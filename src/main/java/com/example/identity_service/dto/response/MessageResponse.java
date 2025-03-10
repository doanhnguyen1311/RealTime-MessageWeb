package com.example.identity_service.dto.response;

import com.example.identity_service.entity.Messages;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    private Long id;
    private String content;
    private String senderName; // Không dùng `User` entity để tránh lỗi Lazy
    // Getters và Setters

    public MessageResponse(Messages message) {
        this.id = message.getId();
        this.content = message.getContent();
        this.senderName = message.getSender().getUsername(); // Lấy trực tiếp
    }
}
