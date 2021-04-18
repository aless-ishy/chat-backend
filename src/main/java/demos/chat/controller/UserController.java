package demos.chat.controller;

import demos.chat.dto.UserDTO;
import demos.chat.entity.User;
import demos.chat.exceptions.UserNotFoundException;
import demos.chat.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping({"/users"})
public class UserController {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    UserController(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<User> findAll(){
        return userRepository.findAll();
    }

    @GetMapping("{id}")
    public UserDTO findById(@PathVariable int id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent())
            return user.get().toDTO(this.modelMapper);
        throw new UserNotFoundException();
    }

    @PatchMapping
    public UserDTO update(Principal principal, @RequestBody @Valid UserDTO userUpdate){
        Optional<User> optionalUser = userRepository.findByEmail(principal.getName());
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setName(userUpdate.getName());
            return userRepository.save(user).toDTO(modelMapper);
        }
        throw new UserNotFoundException();
    }

    @DeleteMapping
    public ResponseEntity<?> delete(Principal principal){
        Optional<User> optionalUser = userRepository.findByEmail(principal.getName());
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            userRepository.delete(user);
            return ResponseEntity.ok().build();
        }
        throw new UserNotFoundException();
    }


}
