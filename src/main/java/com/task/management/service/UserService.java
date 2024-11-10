package com.task.management.service;

import com.task.management.auth.CustomUserDetails;
import com.task.management.entity.User;
import com.task.management.mapper.UserMapper;
import com.task.management.model.UserRequest;
import com.task.management.model.UserRespone;
import com.task.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final UserMapper mapper = UserMapper.INSTANCE;
    public String register(UserRequest userRequest) {
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userRepository.save(User.builder().email(userRequest.getEmail()).role(userRequest.getRole()).username(userRequest.getUsername()).password(userRequest.getPassword()).build());
        return "User registered successfully!";
    }
    public List<UserRespone> getUsers(){
        return mapper.userListToUserResponseList(userRepository.findAll());
    }

}
