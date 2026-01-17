package de.othr.hwwa.controller;

import de.othr.hwwa.config.JwtUtil;
import de.othr.hwwa.model.jwt.ApiUser;
import de.othr.hwwa.model.jwt.LoginRequest;
import de.othr.hwwa.service.ApiUserServiceI;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final JwtUtil jwtUtil;
    private final ApiUserServiceI apiUserService;

    public ApiAuthController(JwtUtil jwtUtil, ApiUserServiceI apiUserService) {
        this.jwtUtil = jwtUtil;
        this.apiUserService = apiUserService;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {

        ApiUser user = apiUserService.authenticate(
                request.getEmail(),
                request.getPassword()
        );

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getCompanyId()
        );

        return Map.of("token", token);
    }
}
