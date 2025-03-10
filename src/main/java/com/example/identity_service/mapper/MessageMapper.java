package com.example.identity_service.mapper;

import com.example.identity_service.dto.response.MessageResponse;
import com.example.identity_service.entity.Messages;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageResponse toMessageResponse(Messages message);
}
