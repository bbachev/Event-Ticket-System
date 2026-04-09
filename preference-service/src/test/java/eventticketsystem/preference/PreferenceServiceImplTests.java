package eventticketsystem.preference;


import eventticketsystem.preference.dto.PreferenceCategory;
import eventticketsystem.preference.dto.PreferenceRequest;
import eventticketsystem.preference.dto.UserDetailsRequest;
import eventticketsystem.preference.entity.UserEntity;
import eventticketsystem.preference.entity.UserPreferenceEntity;
import eventticketsystem.preference.exception.UserAlreadyExistsException;
import eventticketsystem.preference.exception.UserNotExistsException;
import eventticketsystem.preference.repository.UserPreferenceRepository;
import eventticketsystem.preference.repository.UserRepository;
import eventticketsystem.preference.service.impl.PreferenceServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PreferenceServiceImplTests {
    @Mock
    private UserPreferenceRepository userPreferenceRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PreferenceServiceImpl preferenceService;

    @Test
    public void testAddUserDetailsShouldThrowWhenUserAlreadyExists() {
        when(this.userRepository.existsById(any(UUID.class))).thenThrow(UserAlreadyExistsException.class);

        UUID userId = UUID.randomUUID();
        UserDetailsRequest request = new UserDetailsRequest("test", "test", "test@test.com");
        assertThrowsExactly(UserAlreadyExistsException.class, () -> this.preferenceService.addUserDetails(userId, request));
    }

    @Test
    public void testAddUserDetailsShouldAdd(){
        when(this.userRepository.existsById(any(UUID.class))).thenReturn(false);
        when(this.userRepository.save(any(UserEntity.class))).thenReturn(new UserEntity());

        this.preferenceService.addUserDetails(UUID.randomUUID(), new UserDetailsRequest("test", "test", "test@test.com"));
        Mockito.verify(this.userRepository).save(any(UserEntity.class));
    }

    @Test
    public void testAddUserPreferencesShouldThrowWhenUserDoesNotExist(){
        when(this.userRepository.findById(any(UUID.class))).thenThrow(UserNotExistsException.class);
        PreferenceRequest request = new PreferenceRequest(List.of(PreferenceCategory.SPORTS, PreferenceCategory.THEATRE));
        assertThrowsExactly(UserNotExistsException.class, () -> this.preferenceService.addUserPreferences(UUID.randomUUID(), request));
    }

    @Test
    public void testAddUserPreferencesShouldAdd(){
        UUID id = UUID.randomUUID();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setEmail("test@test.com");
        when(this.userRepository.findById(any(UUID.class))).thenReturn(Optional.of(userEntity));

        PreferenceRequest request = new PreferenceRequest(List.of(PreferenceCategory.SPORTS, PreferenceCategory.THEATRE));
        when(userPreferenceRepository.existsByUserIdAndCategory(any(UUID.class), any(PreferenceCategory.class))).thenReturn(false);

        this.preferenceService.addUserPreferences(id, request);

        Mockito.verify(userPreferenceRepository, Mockito.times(2)).save(any(UserPreferenceEntity.class));
    }
    @Test
    public void testGetUserPreferencesShouldThrowWhenUserDoesNotExist(){
        when(this.userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrowsExactly(UserNotExistsException.class, () -> this.preferenceService.getUserPreferences(UUID.randomUUID()));
    }

    @Test
    public void testAddUserPreferencesShouldNotSaveWhenAlreadyExists(){
        UUID id = UUID.randomUUID();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        when(this.userRepository.findById(any(UUID.class))).thenReturn(Optional.of(userEntity));

        PreferenceRequest request = new PreferenceRequest(List.of(PreferenceCategory.SPORTS));
        when(userPreferenceRepository.existsByUserIdAndCategory(any(UUID.class), any(PreferenceCategory.class))).thenReturn(true);

        this.preferenceService.addUserPreferences(id, request);

        Mockito.verify(userPreferenceRepository, Mockito.never()).save(any(UserPreferenceEntity.class));
    }
}
