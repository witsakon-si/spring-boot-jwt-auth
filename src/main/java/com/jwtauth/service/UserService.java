package com.jwtauth.service;

import com.jwtauth.dto.UserDto;
import com.jwtauth.entity.User;
import com.jwtauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public List<UserDto> allUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        List<UserDto> userDtos = modelMapper.map(users, new TypeToken<List<UserDto>>() {
        }.getType());

        return userDtos;
    }
}
