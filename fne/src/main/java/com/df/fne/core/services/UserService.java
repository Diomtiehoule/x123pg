package com.df.fne.core.services;

import com.df.fne.core.domaines.UserDto;
import com.df.fne.presenter.request.LoginRequest;

import java.util.Map;

public interface UserService extends BaseService<UserDto> {
    UserDto findByUsername(String username);
    Map<String , Object> register(UserDto userDto);
    Map<String, Object> login(LoginRequest loginRequest);
}
