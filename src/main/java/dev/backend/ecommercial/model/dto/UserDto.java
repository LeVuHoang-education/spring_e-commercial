package dev.backend.ecommercial.model.dto;

import dev.backend.ecommercial.model.entity.User;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String roleName;

    public UserDto (Long id,String username, String email, String phone) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;

    }

    public static UserDto to(User user) {
        return UserDto.builder()
                .id(user.getUserId())
                .email(user.getEmail())
                .username(user.getUserName())
                .phone(user.getPhone())
                .roleName(user.getRole() != null ? user.getRole().getName() : null)
                .build();
    }
}
