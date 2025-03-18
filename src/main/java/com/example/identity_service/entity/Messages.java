package com.example.identity_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Messages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver; // null nếu là nhóm

    @ManyToOne
    private ChatRoom chatRoom; // null nếu là tin nhắn 1-1

    private String content;

    @Enumerated(EnumType.STRING)
    private MessageStatus status; // SENT, DELIVERED(đã gửi), READ(đã xem)

    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
    }
}
