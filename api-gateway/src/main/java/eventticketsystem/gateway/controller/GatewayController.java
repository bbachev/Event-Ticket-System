package eventticketsystem.gateway.controller;

import eventticketsystem.gateway.dto.gateway.LoginRequest;
import eventticketsystem.gateway.dto.gateway.RegisterUser;
import eventticketsystem.gateway.exception.UserAlreadyExistsException;
import eventticketsystem.gateway.exception.UserNotExistsException;
import eventticketsystem.gateway.exception.WrongUsernameOrPasswordException;
import eventticketsystem.gateway.service.GatewayService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("gateway")
@RestController
public class GatewayController {
    private final GatewayService gatewayService;

    public GatewayController(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @PostMapping("register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterUser registerUser){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(gatewayService.registerUser(registerUser));
        } catch (UserAlreadyExistsException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request){
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(gatewayService.loginUser(request));
        } catch (WrongUsernameOrPasswordException | UserNotExistsException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
