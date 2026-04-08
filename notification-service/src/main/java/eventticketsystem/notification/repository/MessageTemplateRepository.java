package eventticketsystem.notification.repository;

import eventticketsystem.notification.entity.MessageTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageTemplateRepository extends JpaRepository<MessageTemplateEntity, String> {
}
