package com.df.fne.core.mappers;

import com.df.fne.core.domaines.NotificationDto;
import com.df.fne.jpa.entities.Notification;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationDto toDto(Notification notification);
    Notification toEntity(NotificationDto notificationDto);
    List<NotificationDto> toDtoList(List<Notification> notification);
    List<Notification> toEntityList(List<NotificationDto> notificationsDto);
}
