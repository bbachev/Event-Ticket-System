package eventticketsystem.gateway.mapper;

import eventticketsystem.gateway.dto.gateway.RegisterUser;
import eventticketsystem.gateway.dto.gateway.User;
import eventticketsystem.gateway.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.time.OffsetDateTime;
import java.util.UUID;

@Mapper(imports = {UUID.class, OffsetDateTime.class, BCrypt.class})
public interface GatewayMapper {
    @Mapping(target = "password", expression = "java(BCrypt.hashpw(request.password(), BCrypt.gensalt()))")
    @Mapping(target = "role", expression = "java(UserRole.USER)")
    @Mapping(target = "createdAt", expression = "java(OffsetDateTime.now())")
    UserEntity toEntity(RegisterUser request);

    User toDto(UserEntity userEntity);
}
