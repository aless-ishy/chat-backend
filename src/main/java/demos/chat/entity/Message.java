package demos.chat.entity;

import demos.chat.dto.MessageDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String text;

    @ManyToOne
    private Registration registration;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date created = new Date();

    public Message(String text, Registration registration){
        this.text = text;
        this.registration = registration;
    }

    public MessageDTO toMessageDTO(){
        User user = this.registration.getUser();
        return new MessageDTO(this.id,this.text,this.created,user.getName(),user.getEmail());
    }
}
