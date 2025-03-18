package com.example.identity_service.entity;

import lombok.*;
import org.springframework.data.neo4j.core.schema.*;

import java.time.LocalDateTime;

@Node("Message")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessagesNode {
    @Id
    @GeneratedValue
    private Long id;

    @Property("content")
    private String content;

    @Property("timestamp")
    private LocalDateTime timestamp;
}
