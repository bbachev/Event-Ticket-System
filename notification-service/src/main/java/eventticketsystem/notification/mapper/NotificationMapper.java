package eventticketsystem.notification.mapper;

import eventticketsystem.notification.dto.MessageTemplate;
import eventticketsystem.notification.entity.MessageTemplateEntity;
import org.mapstruct.Mapper;

@Mapper
public interface NotificationMapper {
    MessageTemplate toDTo(MessageTemplateEntity template);

}
