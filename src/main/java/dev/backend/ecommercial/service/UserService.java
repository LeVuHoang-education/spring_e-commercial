package dev.backend.ecommercial.service;

import dev.backend.ecommercial.model.dto.UserDto;
import dev.backend.ecommercial.model.entity.User;
import dev.backend.ecommercial.model.payload.request.ChangePassword;
import dev.backend.ecommercial.model.payload.response.ResponseData;
import dev.backend.ecommercial.model.payload.response.ResponseError;
import dev.backend.ecommercial.repository.RoleRepository;
import dev.backend.ecommercial.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseData<List<UserDto>> getAll() {
        List<UserDto> listUsers = userRepository.findAll().stream()
                .map(UserDto::to)
                .filter(user -> !user.getRoleName().equals("ROLE_MANAGER"))
                .collect(Collectors.toList());
        return new ResponseData<>(200, "Retrieved all users successfully", listUsers);
    }

    public ResponseData getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        UserDto userDto = UserDto.to(user);
        return new ResponseData<>(200, "Retrieved user successfully", userDto);
    }

    public ResponseData getProfile(Authentication authentication) {
        String email = authentication.getName(); // Lấy email từ token

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserDto userDto = UserDto.to(user);
        return new ResponseData<>(200, "Retrieved user successfully", userDto);
    }

    public ResponseData<String> delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        userRepository.delete(user);
        return new ResponseData<>(200, "Deleted user successfully", null);
    }

    public ResponseData<UserDto> update(Long id, UserDto updateUserDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (updateUserDto.getUsername() != null) {
            user.setUserName(updateUserDto.getUsername());
        }
        if (updateUserDto.getEmail() != null) {
            user.setEmail(updateUserDto.getEmail());
        }
        if (updateUserDto.getPhone() != null) {
            user.setPhone(updateUserDto.getPhone());
        }

        try {
            userRepository.save(user);
            UserDto responseDto = new UserDto(user.getUserId(), user.getUserName(), user.getEmail(), user.getPhone());
            return new ResponseData<>(200, "Updated user successfully", responseDto);
        } catch (Exception e) {
            return new ResponseData<>(500, "Failed to update user: " + e.getMessage(), null);
        }
    }

    public ResponseData<String> changePassword(ChangePassword request, Principal connectedUser) {
        User user = userRepository.findByEmail(connectedUser.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return new ResponseError<>(400, "Current password is incorrect");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return new ResponseError<>(400, "New password and confirm password do not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return new ResponseData<>(200, "Password changed successfully");
    }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Tìm người dùng dựa trên email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email: " + email));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRole().getName())
                .build();
    }

}
