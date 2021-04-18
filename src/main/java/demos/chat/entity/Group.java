package demos.chat.entity;

import demos.chat.dto.GroupDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Entity(name="Groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @NotBlank(message = "Name is mandatory.")
    private String name;

    @OneToMany(mappedBy = "group",cascade = CascadeType.REMOVE)
    private List<Registration> registrations =  new ArrayList<>();

    public GroupDTO toGroupDTO(){
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId(this.id);
        groupDTO.setName(this.name);
        groupDTO.setUsers(this.registrations.stream().map(Registration::toUserRegistrationDTO).collect(Collectors.toList()));
        return groupDTO;
    }
}
