package com.ottention.banana.mapper;

import com.ottention.banana.dto.response.NotificationResponse;
import com.ottention.banana.entity.notification.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SseMapper {
    NotificationResponse toResponse(Notification notification);
}
