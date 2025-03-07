package com.example.identity_service.node;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserNode {
    @Id
    private String id;

    private String name;
}
