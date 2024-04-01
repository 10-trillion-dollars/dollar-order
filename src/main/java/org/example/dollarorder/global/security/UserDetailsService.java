package org.example.dollarorder.global.security;

import lombok.RequiredArgsConstructor;
import org.example.dollarorder.domain.entity.User;
import org.example.dollarorder.domain.entity.UserRoleEnum;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsService {


    public UserDetails getUser(Long userId, String username, String email, UserRoleEnum role)
        throws UsernameNotFoundException {
        User user = new User(userId, username, email, role);
        return new UserDetailsImpl(user);
    }
}
