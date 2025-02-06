package dev.backend.ecommercial.model.dto;

import dev.backend.ecommercial.model.entity.User;
import lombok.*;

@Data
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String roleName;

    public static UserDto to(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUserName())
                .phone(user.getPhone())
                .roleName(user.getRole() != null ? user.getRole().getName() : null)
                .build();
    }
}
