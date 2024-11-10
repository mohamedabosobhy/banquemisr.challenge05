package com.task.management.controller;

import com.task.management.entity.User;
import com.task.management.exception.TaskManagementEnum;
import com.task.management.exception.TaskManagementException;
import com.task.management.model.AuthRequest;
import com.task.management.model.AuthResponse;
import com.task.management.repository.UserRepository;
import com.task.management.auth.AuthJwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {


    @Autowired
    private AuthJwtToken jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/auth")
    public AuthResponse login(@RequestBody AuthRequest authRequest) throws TaskManagementException {
        User user = userRepository.findByUsername(authRequest.getUsername()).orElseThrow(TaskManagementEnum.TASK_MANAGEMENT_EXCEPTION_ENUM_06::getValue);
        if(! passwordEncoder.matches(authRequest.getPassword(),user.getPassword())){
            throw TaskManagementEnum.TASK_MANAGEMENT_EXCEPTION_ENUM_05.getValue();
        }
        Map<String,Object> claims = new HashMap<>();
        claims.put("id",user.getId().toString());
        claims.put("role",user.getRole());
        claims.put("username",user.getUsername());
        return AuthResponse.builder().token(jwtTokenUtil.generateToken(authRequest.getUsername(),claims)).role(user.getRole()).build();
    }


}
