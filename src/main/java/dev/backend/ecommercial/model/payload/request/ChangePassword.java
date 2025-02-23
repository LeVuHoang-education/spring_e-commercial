package dev.backend.ecommercial.model.payload.request;
import lombok.Data;


@Data

public class ChangePassword {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
