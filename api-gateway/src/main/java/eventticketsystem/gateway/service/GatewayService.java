package eventticketsystem.gateway.service;

import eventticketsystem.gateway.dto.gateway.LoginRequest;
import eventticketsystem.gateway.dto.gateway.LoginResponse;
import eventticketsystem.gateway.dto.gateway.RegisterUser;
import eventticketsystem.gateway.dto.gateway.User;

public interface GatewayService {
    User registerUser(RegisterUser registerUser);
    LoginResponse loginUser(LoginRequest request);
}
