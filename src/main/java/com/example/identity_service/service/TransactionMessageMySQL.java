package com.example.identity_service.service;

import com.example.identity_service.dto.response.MessageResponse;
import com.example.identity_service.entity.MessageStatus;
import com.example.identity_service.entity.Messages;
import com.example.identity_service.entity.User;
import com.example.identity_service.mapper.MessageMapper;
import com.example.identity_service.mapper.UserMapper;
import com.example.identity_service.node_repository.MessagesNodeRepository;
import com.example.identity_service.repository.MessagesRepository;
import com.example.identity_service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;

@Service
@Slf4j
public class TransactionMessgaeMySQL {
    private final MessagesRepository messageRepository;
    private final UserRepository userRepository;
    private final MessagesNodeRepository messageNodeRepository;
    private final MessageMapper messageMapper;
    private final UserMapper userMapper;

    public TransactionMessgaeMySQL(MessagesRepository messageRepository, UserRepository userRepository, MessagesNodeRepository messageNodeRepository, MessageMapper messageMapper, UserMapper userMapper) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.messageNodeRepository = messageNodeRepository;
        this.messageMapper = messageMapper;
        this.userMapper = userMapper;
    }

    @Transactional
    public MessageResponse sendMessageForMySQL(String senderId, String receiverId, String message) {
        try {
//            log.info("Sender ID: {}", senderId);
//            log.info("Receiver ID: {}", receiverId);
//            log.info("Message Content: {}", message);

            User sender = userRepository.findById(senderId)
                    .orElseThrow(() -> new RuntimeException("Sender not found"));
            User receiver = userRepository.findById(receiverId)
                    .orElseThrow(() -> new RuntimeException("Receiver not found"));

//            log.info("Sender: {}", sender);
//            log.info("Receiver: {}", receiver);

            Messages messages = Messages.builder()
                    .sender(sender)
                    .receiver(receiver)
                    .content(message)
                    .status(MessageStatus.SENT)
                    .timestamp(LocalDateTime.now())
                    .build();

//            log.info("Message before saving: {}", messages);
            log.info("Is transaction before active? {}", TransactionSynchronizationManager.isActualTransactionActive());
            Messages savedMessages = messageRepository.saveAndFlush(messages);
            log.info("Is transaction after active? {}", TransactionSynchronizationManager.isActualTransactionActive());
//            log.info("Message saved: {}", savedMessages);


            return messageMapper.toMessageResponse(savedMessages);
        } catch (Exception e) {
            log.error("Error saving message: {}", e.getMessage());
            throw new RuntimeException("Failed to send message", e); // 👈 Thêm dòng này để Spring rollback đúng
        }
    }
}
