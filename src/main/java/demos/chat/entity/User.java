package demos.chat.entity;

import demos.chat.dto.UserDTO;
import lombok.*;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Data
@NoArgsConstructor
@Entity(name="Users")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    @NotBlank(message = "Name is mandatory.")
    private String name;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "E-mail is mandatory.")
    @Email(message = "Invalid e-mail.")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Password is mandatory.")
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Registration> registrations = new ArrayList<>();

    @Transient
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.PRIVATE)
    private boolean skip = false;

    public void setEmail(String email){
        if(!this.skip)
            this.email = email;
    }

    public void setId(int id){
        if(!this.skip)
            this.id = id;
    }

    public void setPassword(String password){
        if(!this.skip)
            this.password = password;
    }

    public UserDTO toDTO(ModelMapper modelMapper){
        this.setSkip(true);
        UserDTO userDTO = modelMapper.map(this, UserDTO.class);
        this.setSkip(false);
        return userDTO;
    }
}

