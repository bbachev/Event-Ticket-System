package eventticketsystem.preference.repository;

import eventticketsystem.preference.dto.PreferenceCategory;
import eventticketsystem.preference.entity.UserPreferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreferenceEntity, UUID> {
    List<UserPreferenceEntity> findAllByUserId(UUID userId);
    boolean existsByUserIdAndCategory(UUID userId, PreferenceCategory category);
    List<UserPreferenceEntity> findAllByCategory(PreferenceCategory category);

}
