package com.example.identity_service.controller;

import com.example.identity_service.dto.response.APIResponse;
import com.example.identity_service.entity.User;
import com.example.identity_service.node.UserNode;
import com.example.identity_service.node_repository.UserNodeRepository;
import com.example.identity_service.service.NodeService;
import com.example.identity_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usernode")
public class UserNodeController {

    @Autowired
    private NodeService nodeService;

    @GetMapping("/list")
    public APIResponse<?> getUserNode(){
        return APIResponse.<List<UserNode>>builder().data(nodeService.getAllUserNodes()).build();
    }

    @GetMapping("/friendOf/{id}")
    public APIResponse<?> ListFriendOfUser(@PathVariable String id){
        return APIResponse.<List<UserNode>>builder().data(nodeService.getFriendsOfUser(id)).build();
    }
}
