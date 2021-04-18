package demos.chat.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class GroupDTO {
    private int id;
    private String name;
    private List<UserRegistrationDTO> users = new ArrayList<>();
}
