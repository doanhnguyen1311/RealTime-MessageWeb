package com.example.identity_service.controller;

import com.example.identity_service.entity.Messages;
import com.example.identity_service.service.MessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageRestController {
    private final MessageService messageService;

    public MessageRestController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/{senderId}/{receiverId}")
    public List<Messages> getMessages(@PathVariable String senderId, @PathVariable String receiverId) {
        return messageService.getMessages(senderId, receiverId);
    }
}
