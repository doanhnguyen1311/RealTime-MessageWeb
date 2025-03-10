package com.example.identity_service.node_repository;

import com.example.identity_service.entity.Messages;
import com.example.identity_service.entity.MessagesNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessagesNodeRepository extends Neo4jRepository<MessagesNode, Long> {
    @Query("MATCH (a:user {id: $senderId}), (b:user {id: $receiverId})\n" +
            "MERGE (a)-[:SENT_MESSAGE]->(m:Message {content: $content, timestamp: datetime()})-[:TO]->(b)\n")
    Messages saveMessage(@Param("senderId") String senderId, @Param("receiverId") String receiverId, @Param("content") String content);
}
