package com.df.fne.jpa.serviceImpl;

import com.df.fne.core.domaines.enums.Role;
import com.df.fne.core.domaines.UserDto;
import com.df.fne.core.exceptions.BadRequestException;
import com.df.fne.core.mappers.UserMapper;
import com.df.fne.core.services.UserService;
import com.df.fne.jpa.entities.User;
import com.df.fne.jpa.repositories.UserRepository;
import com.df.fne.presenter.request.LoginRequest;
import com.df.fne.presenter.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(JwtService jwtService , UserRepository userRepository , PasswordEncoder passwordEncoder , UserMapper userMapper){
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto findByUsername(String username) {
        return null;
    }

    @Override
    public Map<String, Object> register(UserDto userDto) {
        Map<String, Object> map = new HashMap<>();

        if (!userDto.isValid()) {
            throw new BadRequestException("Veuillez renseigner correctement les champs");
        }

        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new BadRequestException("Username already used");
        }
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new BadRequestException("Email already used");
        }

        User user = User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .enabled(true)
                .role(Role.USER)
                .build();
        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(savedUser, savedUser.getId());
        UserDto savedUserDto = userMapper.toDto(savedUser);
        map.put("status", true);
        map.put("message", "Création de compte avec succès");
        map.put("token", token);
        map.put("userId", savedUserDto.getId());
        return map;
    }


    @Override
    public Map<String, Object> login(LoginRequest loginRequest) {
        Map<String, Object> map = new HashMap<>();
        try {
            Optional<User> userOptional = userRepository.findByEmail(loginRequest.email());

            if (userOptional.isEmpty()) {
                throw new BadRequestException("Invalid email or password");
            }

            User user = userOptional.get();
            if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
                throw new BadRequestException("password incorrect.");
            }
            String token = jwtService.generateToken(user, user.getId());
            //UserDto userDto = userMapper.toDto(user);

            map.put("status", true);
            map.put("message", "Connection successfully");
            map.put("token", token);
            map.put("expirationDate", jwtService.extractExpiration(token));
        } catch (Exception ex) {
            map.put("status", false);
            map.put("message", ex.getMessage());
            map.put("token", null);
        }
        return map;
    }

    @Override
    public UserDto create(UserDto userDto) {
        return null;
    }

    @Override
    public UserDto update(UserDto userDto, UUID id) {
        return null;
    }

    @Override
    public UserDto get(UUID id) {
        return null;
    }

    @Override
    public List<UserDto> getAll() {
        return List.of();
    }

    @Override
    public void delete(UUID id) {

    }
}
