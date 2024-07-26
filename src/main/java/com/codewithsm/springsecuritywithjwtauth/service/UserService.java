package com.codewithsm.springsecuritywithjwtauth.service;

import com.codewithsm.springsecuritywithjwtauth.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepo userRepo;

//    private static final Logger log = LoggerFactory.getLogger(UserService.class); // efficient for debug with console log

//    non efficient========
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return userRepo.findByEmail(username).orElseThrow();
//    }

//    efficient=================
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByEmail(username)
                .orElseThrow(() -> {
//                    log.warn("User not found with email: {}", username);
                    return new UsernameNotFoundException("User not found with email: " + username);
                });
}

}
