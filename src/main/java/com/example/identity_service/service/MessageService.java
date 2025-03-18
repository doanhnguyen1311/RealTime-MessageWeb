package com.example.identity_service.service;

import com.example.identity_service.dto.response.MessageResponse;
import com.example.identity_service.entity.MessageStatus;
import com.example.identity_service.entity.Messages;
import com.example.identity_service.entity.User;
import com.example.identity_service.mapper.MessageMapper;
import com.example.identity_service.node_repository.MessagesNodeRepository;
import com.example.identity_service.node_repository.MessagesNodeRepository;
import com.example.identity_service.repository.MessagesRepository;
import com.example.identity_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class MessageService {
    private final MessagesRepository messageRepository;
    private final UserRepository userRepository;
    private final MessagesNodeRepository messageNodeRepository;
    private final MessageMapper messageMapper;

    public MessageService(MessagesRepository messageRepository, UserRepository userRepository, MessagesNodeRepository messageNodeRepository, MessageMapper messageMapper) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.messageNodeRepository = messageNodeRepository;
        this.messageMapper = messageMapper;
    }

    public MessageResponse sendMessage(String senderId, String receiverId, String message) {


        User sender = userRepository.findById(senderId).orElseThrow(() -> new RuntimeException("User not found"));
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new RuntimeException("User not found"));

        Messages messages = Messages.builder()
                .sender(sender)
                .receiver(receiver)
                .content(message)
                .status(MessageStatus.SENT)
                .timestamp(LocalDateTime.now())
                .build();

        messageNodeRepository.saveMessage(senderId, receiverId, message);

        Messages saveMessage = messageRepository.save(messages);

        log.info("Message saved to database: {}", saveMessage.toString());

        return messageMapper.toMessageResponse(saveMessage);
    }

    public List<Messages> getMessages(String senderId, String receiverId) {
        User sender = userRepository.findById(senderId).orElse(null);
        User receiver = userRepository.findById(receiverId).orElse(null);
        return messageRepository.findBySenderAndReceiverOrderByTimestamp(sender, receiver);
    }
}
