package com.example.identity_service.node_repository;

import com.example.identity_service.node.UserNode;
import jakarta.websocket.Session;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserNodeRepository extends Neo4jRepository<UserNode, String> {

    @Query("MATCH (u:user) RETURN u")
    List<UserNode> findAllUsers();

    @Query("CREATE (u:user {id: $id, name: $name})")
    UserNode createUserNode(@Param("id") String id, @Param("name") String name);

    @Query("MATCH (a:user {id: $id})-[:FRIEND]-(u:user) RETURN u")
    List<UserNode> findAllFriendOfUser(@Param("id") String id);

    @Query("MERGE (a:user {id: $id1})-[:FRIEND]-(u:user {id: $id2}) ")
    void addFriend(@Param("id1") String id1, @Param("id2") String id2);


    @Query("MATCH (sender:user {id: $id1})-[:FRIEND_REQUEST]->(receiver:user {id: $id2}) " +
            "RETURN sender")
    UserNode findFriendRequestSender(@Param("id1") String id1, @Param("id2") String id2);

    @Query("MATCH (h:user {id:$id1})-[r:FRIEND_REQUEST]-(a:user {id:$id2})\n" +
            "MERGE (h)-[:FRIEND]-(a)\n" +
            "DETACH DELETE (r)")
    UserNode acceptFriendRequest(@Param("id1") String id1, @Param("id2") String id2);



}
