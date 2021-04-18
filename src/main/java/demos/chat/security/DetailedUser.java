package demos.chat.security;

import demos.chat.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class DetailedUser implements UserDetails {
    private User user;
    private Set<Role> authorities;

    public DetailedUser(User user){
        this.user = user;
        this.authorities = new HashSet<>();
        this.authorities.add(new Role());
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
