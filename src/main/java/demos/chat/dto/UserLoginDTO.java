package demos.chat.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class UserLoginDTO {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
