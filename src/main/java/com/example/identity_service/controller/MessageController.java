package com.example.identity_service.controller;


import com.example.identity_service.dto.request.MessageRequest;
import com.example.identity_service.dto.response.APIResponse;
import com.example.identity_service.dto.response.MessageResponse;
import com.example.identity_service.entity.Messages;
import com.example.identity_service.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /*@MessageMapping("/chat")
    @SendTo("/topic/messages")
    public Messages sendMessage(Messages message) {
        log.info("Message: {}", message.getContent());
        log.info("Sender: {}", message.getSender());
        log.info("Receiver: {}", message.getReceiver());
        return messageService.sendMessage(message.getSender().getId(), message.getReceiver().getId(), message.getContent());
    }*/
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public APIResponse<?> sendMessage(@Payload MessageRequest request) {
        System.out.println("Received MessageRequest: " + request); // Log JSON nhận được

        log.info("Received ID: " + request.getReceiverId());
        log.info("SenderID" + request.getSenderId());
        MessageResponse message = messageService.sendMessage(request.getSenderId(), request.getReceiverId(), request.getContent());

        System.out.println("Saved Message: " + message); // Log tin nhắn sau khi lưu
        return APIResponse.builder()
                .data(message)
                .build();
    }


    /*@MessageMapping("/chat")
    @SendTo("/topic/messages")
    public Message handleMessage(@Payload Message message, SimpMessageHeaderAccessor headerAccessor) {
        System.out.println("Received message: " + message);
        return message;
    }*/
}
