package eventticketsystem.gateway.service.impl;

import eventticketsystem.gateway.adapter.PreferenceAdapter;
import eventticketsystem.gateway.dto.gateway.LoginRequest;
import eventticketsystem.gateway.dto.gateway.LoginResponse;
import eventticketsystem.gateway.dto.gateway.RegisterUser;
import eventticketsystem.gateway.dto.gateway.User;
import eventticketsystem.gateway.entity.UserEntity;
import eventticketsystem.gateway.exception.UserAlreadyExistsException;
import eventticketsystem.gateway.exception.UserNotExistsException;
import eventticketsystem.gateway.exception.WrongUsernameOrPasswordException;
import eventticketsystem.gateway.mapper.GatewayMapper;
import eventticketsystem.gateway.repository.UserRepository;
import eventticketsystem.gateway.service.GatewayService;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class GatewayServiceImpl implements GatewayService {
    private static final GatewayMapper MAPPER = Mappers.getMapper(GatewayMapper.class);

    private final UserRepository userRepository;
    private final PreferenceAdapter preferenceAdapter;
    private final JwtServiceImpl jwtServiceImpl;

    public GatewayServiceImpl(UserRepository userRepository, PreferenceAdapter preferenceAdapter,
                              JwtServiceImpl jwtServiceImpl) {
        this.userRepository = userRepository;
        this.preferenceAdapter = preferenceAdapter;
        this.jwtServiceImpl = jwtServiceImpl;
    }

    @Override
    public User registerUser(RegisterUser registerUser) {
        if (this.userRepository.existsByEmail(registerUser.email()))
            throw new UserAlreadyExistsException(registerUser.email());

        UserEntity entity = MAPPER.toEntity(registerUser);

        this.userRepository.save(entity);
        this.preferenceAdapter.addUserDetails(entity);

        return MAPPER.toDto(entity);
    }


    @Override
    public LoginResponse loginUser(LoginRequest request) {
        UserEntity userEntity = this.userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserNotExistsException(request.email()));

        if (!BCrypt.checkpw(request.password(), userEntity.getPassword())) throw new WrongUsernameOrPasswordException();

        String token = this.jwtServiceImpl.generateToken(userEntity);
        return new LoginResponse(token);
    }

}
