package eventticketsystem.gateway;

import eventticketsystem.gateway.adapter.PreferenceAdapter;
import eventticketsystem.gateway.dto.gateway.LoginRequest;
import eventticketsystem.gateway.dto.gateway.RegisterUser;
import eventticketsystem.gateway.entity.UserEntity;
import eventticketsystem.gateway.exception.UserAlreadyExistsException;
import eventticketsystem.gateway.exception.UserNotExistsException;
import eventticketsystem.gateway.exception.WrongUsernameOrPasswordException;
import eventticketsystem.gateway.mapper.GatewayMapper;
import eventticketsystem.gateway.repository.UserRepository;
import eventticketsystem.gateway.service.impl.JwtServiceImpl;
import eventticketsystem.gateway.service.impl.GatewayServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApiGatewayServiceImplTests {
    private static final GatewayMapper MAPPER = Mappers.getMapper(GatewayMapper.class);
    @Mock
    JwtServiceImpl jwtServiceImpl;

    @Mock
    PreferenceAdapter preferenceAdapter;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    private GatewayServiceImpl gatewayService;

    @Test
    public void testRegisterUserShouldThrowWhenEmailExists() {
        when(userRepository.existsByEmail(any(String.class))).thenThrow(UserAlreadyExistsException.class);

        assertThrowsExactly(UserAlreadyExistsException.class, () -> this.gatewayService.registerUser(
                        new RegisterUser("email@test.com", "password", "firstnName", "lastName")
                )
        );
    }

    @Test
    public void testRegisterUserShouldRegisterUser() {
        when(userRepository.existsByEmail(any(String.class))).thenReturn(false);
        RegisterUser registerUser =  new RegisterUser("email@test.com", "password", "firstnName", "lastName");
        UserEntity entity = MAPPER.toEntity(registerUser);

        when(userRepository.save(any(UserEntity.class))).thenReturn(entity);
        this.gatewayService.registerUser(registerUser);
       verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    public void testLoginShouldThrowWhenUSerNotExist() {
        when(userRepository.findByEmail(any(String.class))).thenThrow(UserNotExistsException.class);
        LoginRequest loginRequest = new LoginRequest("email", "password");
        assertThrowsExactly(UserNotExistsException.class, () -> this.gatewayService.loginUser(loginRequest));
    }
    @Test
    public void testLoginUserShouldThrowWhenWrongPassword() {
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(BCrypt.hashpw("correctPassword", BCrypt.gensalt()));

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(userEntity));

        assertThrowsExactly(WrongUsernameOrPasswordException.class, () ->
                gatewayService.loginUser(new LoginRequest("email@test.com", "wrongPassword")));
    }

    @Test
    public void testLoginShouldLoginAndReturnJwt(){
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(BCrypt.hashpw("correctPassword", BCrypt.gensalt()));

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(userEntity));
        when(jwtServiceImpl.generateToken(userEntity)).thenReturn("mockToken");

        this.gatewayService.loginUser(new LoginRequest("email@test.com", "correctPassword"));
    }
}
