package com.example.identity_service.controller;


import com.example.identity_service.entity.Messages;
import com.example.identity_service.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public Messages sendMessage(Messages message) {
        return messageService.sendMessage(message.getSender().getId(), message.getReceiver().getId(), message.getContent());
    }
}
