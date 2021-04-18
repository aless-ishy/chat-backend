package demos.chat.entity;

import demos.chat.dto.RegistrationDTO;
import demos.chat.dto.UserRegistrationDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames={"user_id", "group_id"})})
public class Registration {
    public enum AdminLevel {REMOVED, NONE, ADMIN, SUPER_ADMIN}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    AdminLevel adminLevel;

    @ManyToOne
    private User user;

    @ManyToOne
    private Group group;

    @OneToMany(mappedBy = "registration", cascade = CascadeType.REMOVE)
    private List<Message> messages = new ArrayList<>();

    public Registration(User user, Group group, AdminLevel adminLevel) {
        this.user = user;
        this.group = group;
        this.adminLevel = adminLevel;
    }

    public RegistrationDTO toRegistrationDTO(){
        return new RegistrationDTO(user.getEmail(), adminLevel.toString(), group.getName(), this.group.getId());
    }

    public UserRegistrationDTO toUserRegistrationDTO(){
        return new UserRegistrationDTO(user.getId(), user.getEmail(),user.getName(), adminLevel.toString());
    }
}
