package demos.chat.dto;

import demos.chat.validation.InEnumConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class AddRegistrationDTO {
    private int id;

    @NotEmpty(message = "Invalid User.")
    private String email;

    @NotBlank(message = "Invalid Admin Level.")
    @InEnumConstraint({"NONE", "ADMIN", "SUPER_ADMIN"})
    private String adminLevel;
}
