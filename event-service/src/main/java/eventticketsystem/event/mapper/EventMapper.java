package eventticketsystem.event.mapper;

import eventticketsystem.event.dto.request.CreateEventRequest;
import eventticketsystem.event.entity.EventEntity;
import eventticketsystem.event.dto.response.EventResponse;
import org.mapstruct.Mapper;

@Mapper
public interface EventMapper {
    EventResponse toModel(EventEntity eventEntity);
    EventEntity toEntity(CreateEventRequest request);
}
