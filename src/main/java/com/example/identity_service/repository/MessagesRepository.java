package com.example.identity_service.repository;

import com.example.identity_service.entity.Messages;
import com.example.identity_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessagesRepository extends JpaRepository<Messages, Long> {
    List<Messages> findBySenderAndReceiverOrderByTimestamp(User sender, User receiver); // truy van tin nhan 2 nguoi theo thgian
}
