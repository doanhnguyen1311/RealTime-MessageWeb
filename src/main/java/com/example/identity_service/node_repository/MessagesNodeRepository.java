package com.example.identity_service.node_repository;

import com.example.identity_service.entity.Messages;
import com.example.identity_service.entity.MessagesNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessagesNodeRepository extends Neo4jRepository<MessagesNode, Long> {
    @Query("MATCH (a:User {id: $senderId}), (b:User {id: $receiverId}) " +
            "CREATE (m:Message {content: $content, timestamp: datetime()}) " +
            "CREATE (a)-[:SENT_MESSAGE]->(m)-[:TO]->(b)")
    Messages saveMessage(@Param("senderId") String senderId, @Param("receiverId") String receiverId, @Param("content") String content);
}
