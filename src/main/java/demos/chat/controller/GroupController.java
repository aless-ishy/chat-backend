package demos.chat.controller;

import demos.chat.dto.*;
import demos.chat.entity.Group;
import demos.chat.entity.Message;
import demos.chat.entity.Registration;
import demos.chat.entity.User;
import demos.chat.exceptions.GroupNotFoundException;
import demos.chat.exceptions.LowAdminLevelException;
import demos.chat.exceptions.UserNotFoundException;
import demos.chat.repository.GroupRepository;
import demos.chat.repository.UserRepository;
import demos.chat.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/groups")
public class GroupController {
    private final GroupRepository groupRepository;
    private final GroupService groupService;
    private final UserRepository userRepository;

    private User requestUser;
    private Registration.AdminLevel adminLevel;
    private Group requestGroup;
    private Registration registration;

    @Autowired
    public GroupController(GroupRepository groupRepository, GroupService groupService, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.groupService = groupService;
        this.userRepository = userRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RegistrationDTO create(Principal principal, @Valid @RequestBody Group groupBody) {
        return this.groupService.create(principal.getName(), groupBody);
    }

    @GetMapping("{id}")
    public GroupDTO findById(@PathVariable int id) {
        Optional<Group> optionalGroup = this.groupRepository.findById(id);
        if (optionalGroup.isPresent())
            return optionalGroup.get().toGroupDTO();
        throw new GroupNotFoundException();
    }

    @PatchMapping("{id}")
    public GroupDTO update(Principal principal, @PathVariable int id, @Valid @RequestBody Group groupUpdate) {
        setRequestData(principal.getName(), id);
        if (adminLevel == Registration.AdminLevel.SUPER_ADMIN) {
            requestGroup.setName(groupUpdate.getName());
            return this.groupRepository.save(requestGroup).toGroupDTO();
        }
        throw new LowAdminLevelException();
    }

    @PostMapping("{id}")
    public RegistrationDTO addUser(Principal principal, @PathVariable int id, @Valid @RequestBody AddRegistrationDTO addRegistrationDTO) {
        setRequestData(principal.getName(), id);
        if (adminLevel == Registration.AdminLevel.NONE)
            throw new LowAdminLevelException();
        Registration registration = groupService.addUser(addRegistrationDTO, requestGroup);
        return registration.toRegistrationDTO();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        Optional<Group> optionalGroup = this.groupRepository.findById(id);
        if (optionalGroup.isPresent()) {
            Group group = optionalGroup.get();
            groupRepository.delete(group);
            return ResponseEntity.ok().build();
        }
        throw new GroupNotFoundException();
    }

    @GetMapping("{id}/messages")
    public List<MessageDTO> findAllMessages(Principal principal,
                                            @PathVariable int id,
                                            @RequestParam(defaultValue = "0") int pageNumber,
                                            @RequestParam(defaultValue = "20") int pageSize){
        setRequestData(principal.getName(), id);
        Page<Message> messages = this.groupService.getMessagesFromGroup(this.requestGroup, pageNumber, pageSize);
        return messages.stream().map(Message::toMessageDTO).collect(Collectors.toList());
    }

    @PostMapping("{id}/messages")
    public MessageDTO addMessage(Principal principal, @PathVariable int id, @Valid @RequestBody AddMessageDTO addMessageDTO){
        setRequestData(principal.getName(), id);
        Message message = groupService.createMessage(addMessageDTO.getText(), registration);
        return message.toMessageDTO();
    }

    private void setRequestData(String email, int id){
        this.requestUser = this.userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        this.requestGroup = this.groupRepository.findById(id).orElseThrow(GroupNotFoundException::new);
        this.registration = groupService.getRegistration(this.requestUser, this.requestGroup);
        this.adminLevel = registration.getAdminLevel();
    }

}
