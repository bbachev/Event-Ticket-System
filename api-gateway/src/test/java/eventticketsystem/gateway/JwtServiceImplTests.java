package eventticketsystem.gateway;

import eventticketsystem.gateway.dto.gateway.UserRole;
import eventticketsystem.gateway.entity.UserEntity;
import eventticketsystem.gateway.service.impl.JwtServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceImplTests {

    private JwtServiceImpl jwtService;

    @BeforeEach
    public void setUp() {
        jwtService = new JwtServiceImpl();
        ReflectionTestUtils.setField(jwtService, "secret", "dGVzdFNlY3JldEtleUZvckp3dFRlc3RpbmdQdXJwb3Nlcw==");
        ReflectionTestUtils.setField(jwtService, "expiration", 86400000L);
    }

    @Test
    public void testGenerateTokenShouldReturnToken() {
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setEmail("test@test.com");
        user.setRole(UserRole.USER);

        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    public void testExtractUserIdShouldReturnUserId() {
        UserEntity user = new UserEntity();
        UUID id = UUID.randomUUID();
        user.setId(id);
        user.setEmail("test@test.com");
        user.setRole(UserRole.USER);

        String token = jwtService.generateToken(user);
        String extractedId = jwtService.extractUserId(token);

        assertEquals(id.toString(), extractedId);
    }

    @Test
    public void testExtractRoleShouldReturnRole() {
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setEmail("test@test.com");
        user.setRole(UserRole.USER);

        String token = jwtService.generateToken(user);
        String role = jwtService.extractRole(token);

        assertEquals(UserRole.USER.toString(), role);
    }
}