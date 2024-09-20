package com.example.jabaJuiceServer.services;

import com.example.jabaJuiceServer.entities.User;
import com.example.jabaJuiceServer.entities.UserUserDetails;
import com.example.jabaJuiceServer.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class UserUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public UserUserDetailsService() {
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByFullName(username);

        System.out.println("Decoded Existing Password: " + user.get().getPassword());

//        // Get the Authentication object from the SecurityContextHolder
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        // Extract the received password from the Authentication object
//        String receivedPassword = authentication.getCredentials().toString();
//        System.out.println("Received Password: " + receivedPassword);


        return user.map(UserUserDetails::new)
                .orElseThrow(()->new UsernameNotFoundException("User not found" + username));
    }
}
