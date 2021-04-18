package demos.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private int id;
    private String text;
    private Date created;
    private String name;
    private String email;
}
