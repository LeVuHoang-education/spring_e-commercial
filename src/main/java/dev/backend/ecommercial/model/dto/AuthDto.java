package dev.backend.ecommercial.model.dto;

import dev.backend.ecommercial.model.entity.User;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthDto {
    private Long id;
    private String token;
    private String refreshToken;
    private String status;
    private String result;

    public static AuthDto from(User user, String token, String refreshToken, String status, String result) {
        return AuthDto.builder()
                .id(user.getId())
                .token(token)
                .refreshToken(refreshToken)
                .status(status)
                .result(result)
                .build();
    }
}
