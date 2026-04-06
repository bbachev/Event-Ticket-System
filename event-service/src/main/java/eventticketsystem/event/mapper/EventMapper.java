package eventticketsystem.event.mapper;

import eventticketsystem.event.dto.messaging.EventCreatedMessage;
import eventticketsystem.event.dto.request.EventRequest;
import eventticketsystem.event.entity.EventEntity;
import eventticketsystem.event.dto.response.EventResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface EventMapper {
    EventResponse toModel(EventEntity eventEntity);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status",  expression = "java(eventticketsystem.event.dto.EventStatus.ACTIVE)")
    EventEntity toEntity(EventRequest request);

    EventCreatedMessage toMessageDto(EventEntity eventEntity);
}
