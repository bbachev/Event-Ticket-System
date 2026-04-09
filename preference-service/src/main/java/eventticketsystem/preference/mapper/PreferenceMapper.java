package eventticketsystem.preference.mapper;

import eventticketsystem.preference.dto.User;
import eventticketsystem.preference.dto.UserDetailsRequest;
import eventticketsystem.preference.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper
public interface PreferenceMapper {
    User toDto(UserEntity userEntity);

    @Mapping(target = "id", source = "userId")
    UserEntity toEntity(UUID userId, UserDetailsRequest request);
}
