package demos.chat.security;

import demos.chat.entity.User;
import demos.chat.exceptions.UserNotFoundException;
import demos.chat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class DetailedUserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public DetailedUserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByEmail(username).orElseThrow(UserNotFoundException::new);
        return new DetailedUser(user);
    }
}
