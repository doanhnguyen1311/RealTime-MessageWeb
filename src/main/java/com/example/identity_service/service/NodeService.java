package com.example.identity_service.service;

import com.example.identity_service.node.UserNode;
import com.example.identity_service.node_repository.UserNodeRepository;
import org.neo4j.driver.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NodeService {

    private final Driver driver;
    private final UserNodeRepository userNodeRepository;

    public NodeService(Driver driver, UserNodeRepository userNodeRepository) {
        this.driver = driver;
        this.userNodeRepository = userNodeRepository;
    }

    @Transactional(readOnly = true)
    public void someMethod() {
        try (var session = driver.session()) {
            var result = session.run("MATCH (n) RETURN n LIMIT 5");
            result.list().forEach(record -> System.out.println(record.asMap()));
        }
    }

    @Transactional(readOnly = true)
    public List<UserNode> getAllUserNodes() {
        return userNodeRepository.findAllUsers();
    }

    public List<UserNode> getFriendsOfUser(String id){
        return userNodeRepository.findAllFriendOfUser(id);
    }

    public UserNode findFriendRequestSender(String id1, String id2){
        return userNodeRepository.findFriendRequestSender(id1, id2);
    }

    public UserNode acceptFriendRequest(String id1, String id2){
        return userNodeRepository.findFriendRequestSender(id1, id2);
    }
}
