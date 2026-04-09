package eventticketsystem.preference.service.impl;

import eventticketsystem.preference.dto.*;
import eventticketsystem.preference.entity.UserEntity;
import eventticketsystem.preference.entity.UserPreferenceEntity;
import eventticketsystem.preference.exception.UserAlreadyExistsException;
import eventticketsystem.preference.exception.UserNotExistsException;
import eventticketsystem.preference.mapper.PreferenceMapper;
import eventticketsystem.preference.repository.UserPreferenceRepository;
import eventticketsystem.preference.repository.UserRepository;
import eventticketsystem.preference.service.PreferenceService;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PreferenceServiceImpl implements PreferenceService {
    private static final PreferenceMapper MAPPER = Mappers.getMapper(PreferenceMapper.class);

    private final UserPreferenceRepository userPreferenceRepository;
    private final UserRepository userRepository;

    public PreferenceServiceImpl(UserPreferenceRepository userPreferenceRepository, UserRepository userRepository) {
        this.userPreferenceRepository = userPreferenceRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<User> searchByPreference(PreferenceCategory preference) {
        return this.userPreferenceRepository.findAllByCategory(preference).stream()
                .map(UserPreferenceEntity::getUser)
                .map(MAPPER::toDto)
                .toList();
    }

    @Override
    public UserPreference getUserPreferences(UUID userId) {
        UserEntity userEntity = this.userRepository.findById(userId).orElseThrow(() -> new UserNotExistsException(userId));

        List<PreferenceCategory> preferences = this.userPreferenceRepository.findAllByUserId(userId).stream()
                .map(UserPreferenceEntity::getCategory).toList();

        return new UserPreference(MAPPER.toDto(userEntity), preferences);
    }

    @Override
    public void addUserPreferences(UUID userId, PreferenceRequest request) {
        UserEntity userEntity = this.userRepository.findById(userId).orElseThrow(() -> new UserNotExistsException(userId));

        request.preferences().forEach(category -> {
            if (!userPreferenceRepository.existsByUserIdAndCategory(userId, category)) {
                UserPreferenceEntity userPreferenceEntity = new UserPreferenceEntity();
                userPreferenceEntity.setUser(userEntity);
                userPreferenceEntity.setCategory(category);
                userPreferenceEntity.setCreatedAt(OffsetDateTime.now());

                this.userPreferenceRepository.save(userPreferenceEntity);
            }
        });


    }

    @Override
    public void addUserDetails(UUID userId, UserDetailsRequest request) {
        if (this.userRepository.existsById(userId)) throw new UserAlreadyExistsException(userId);

        UserEntity entity = MAPPER.toEntity(userId, request);
        this.userRepository.save(entity);
    }
}
