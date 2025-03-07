package com.example.identity_service.service;

import com.example.identity_service.entity.MessageStatus;
import com.example.identity_service.entity.Messages;
import com.example.identity_service.entity.User;
import com.example.identity_service.node_repository.MessagesNodeRepository;
import com.example.identity_service.node_repository.MessagesNodeRepository;
import com.example.identity_service.repository.MessagesRepository;
import com.example.identity_service.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {
    private final MessagesRepository messageRepository;
    private final UserRepository userRepository;
    private final MessagesNodeRepository messageNodeRepository;

    public MessageService(MessagesRepository messageRepository, UserRepository userRepository, MessagesNodeRepository messageNodeRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.messageNodeRepository = messageNodeRepository;
    }

    public Messages sendMessage(String senderId, String receiverId, String message) {
        User sender = userRepository.findById(senderId).orElse(null);
        User receiver = userRepository.findById(receiverId).orElse(null);

        Messages messages = Messages.builder()
                .sender(sender)
                .receiver(receiver)
                .content(message)
                .status(MessageStatus.SENT)
                .timestamp(LocalDateTime.now())
                .build();

        messageNodeRepository.saveMessage(senderId, receiverId, message);
        return messageRepository.save(messages);
    }

    public List<Messages> getMessages(String senderId, String receiverId) {
        User sender = userRepository.findById(senderId).orElse(null);
        User receiver = userRepository.findById(receiverId).orElse(null);
        return messageRepository.findBySenderAndReceiverOrderByTimestamp(sender, receiver);
    }
}
