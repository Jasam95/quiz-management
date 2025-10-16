package com.example.quiz_management.service.impl;




import com.example.quiz_management.dto.UserDto;
import com.example.quiz_management.entity.Role;
import com.example.quiz_management.entity.User;
import com.example.quiz_management.repository.RoleRepository;
import com.example.quiz_management.repository.UserRepository;
import com.example.quiz_management.service.UserLoginService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


//import org.springframework.security.crypto.password.PasswordEncoder;
import org.modelmapper.ModelMapper;


@Service
@AllArgsConstructor
public class UserLogInServiceImplementation implements UserLoginService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;


    @Override
    public void createUser(UserDto userDto , String roleParticipant) {

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = modelMapper.map(userDto, User.class);
        user.setFullName(userDto.getFullName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = createRoleIfNotExist(roleParticipant);

        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    // Create role if it does not exist
    public Role createRoleIfNotExist(String roleParticipant) {
        return roleRepository.findByName(roleParticipant)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(roleParticipant);
                    return roleRepository.save(role);
                });
    }

    @Override
    public UserDto findUserByEmail(String email) {
        User findUser =  userRepository.findByEmail(email);
        return modelMapper.map(findUser, UserDto.class);
    }

    @Override
    public List<UserDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDto.class)).toList();
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    @Override
    public long countByRoleName(String participant) {
        return userRepository.countByRoleName(participant);
    }

}