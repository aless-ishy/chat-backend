package demos.chat.controller;

import demos.chat.entity.User;
import demos.chat.exceptions.EmailInUseException;
import demos.chat.exceptions.UserNotFoundException;
import demos.chat.repository.UserRepository;
import demos.chat.security.SecurityConstants;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class PublicController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public PublicController(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @PostMapping(SecurityConstants.SIGN_UP_URL)
    public ResponseEntity<?> create(@RequestBody @Valid User user){
        Optional<User> existingUser = this.userRepository.findByEmail(user.getEmail());
        if(existingUser.isPresent())
            throw new EmailInUseException();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return new ResponseEntity<>(userRepository.save(user).toDTO(modelMapper), HttpStatus.CREATED);
    }
}
