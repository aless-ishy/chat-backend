package demos.chat.service;

import demos.chat.dto.AddRegistrationDTO;
import demos.chat.dto.RegistrationDTO;
import demos.chat.entity.Group;
import demos.chat.entity.Message;
import demos.chat.entity.Registration;
import demos.chat.entity.User;
import demos.chat.exceptions.RegistrationNotFound;
import demos.chat.exceptions.UserNotFoundException;
import demos.chat.repository.GroupRepository;
import demos.chat.repository.MessageRepository;
import demos.chat.repository.RegistrationRepository;
import demos.chat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GroupService {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final RegistrationRepository registrationRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public GroupService(UserRepository userRepository, GroupRepository groupRepository, RegistrationRepository registrationRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.registrationRepository = registrationRepository;
        this.messageRepository = messageRepository;
    }


    public RegistrationDTO create(String email, Group groupBody) {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        Group group = groupRepository.save(groupBody);
        Registration registration = new Registration(user, group, Registration.AdminLevel.SUPER_ADMIN);
        this.registrationRepository.save(registration);
        return new RegistrationDTO(user.getEmail(), Registration.AdminLevel.SUPER_ADMIN.toString(), group.getName(), group.getId());
    }

    public Registration addUser(AddRegistrationDTO addRegistrationDTO, Group group) {
        User user = userRepository.findByEmail(addRegistrationDTO.getEmail()).orElseThrow(UserNotFoundException::new);
        Optional<Registration> optionalRegistration = registrationRepository.findByUserAndGroup(user, group);
        Registration.AdminLevel adminLevel = Registration.AdminLevel.valueOf(addRegistrationDTO.getAdminLevel());
        Registration registration;
        if (optionalRegistration.isPresent()) {
            registration = optionalRegistration.get();
            if (registration.getAdminLevel() != adminLevel) {
                registration.setAdminLevel(adminLevel);
                registration = this.registrationRepository.save(registration);
            }
        } else {
            registration = new Registration(user, group, adminLevel);
            this.registrationRepository.save(registration);
        }
        return registration;
    }

    public Registration getRegistration(User user, Group group) {
        Registration registration =  registrationRepository.findByUserAndGroup(user, group).orElseThrow(RegistrationNotFound::new);
        if(registration.getAdminLevel() == Registration.AdminLevel.REMOVED)
            throw new RegistrationNotFound();
        return registration;
    }

    public Message createMessage(String text, Registration registration) {
        Message message = new Message(text, registration);
        return messageRepository.save(message);
    }

    public Page<Message> getMessagesFromGroup(Group group, int pageNumber, int pageSize){
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        return this.messageRepository.findAllByRegistration_GroupOrderByCreatedAsc(group, paging);
    }
}
