package com.codewithsm.springsecuritywithjwtauth.service;

import com.codewithsm.springsecuritywithjwtauth.dto.ReqRes;
import com.codewithsm.springsecuritywithjwtauth.model.Users;
import com.codewithsm.springsecuritywithjwtauth.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;



@Service
@Transactional
public class AuthService {

    // Logger dependency added for efficient logging
//    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepo userRepo;
    private final JWTUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    // Constructor injection for better testability and immutability
    @Autowired
    public AuthService(UserRepo userRepo, JWTUtils jwtUtils, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepo = userRepo;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public ReqRes signUp(ReqRes registrationRequest){
        ReqRes resp = new ReqRes();

        try {
            Users users = new Users();
            users.setEmail(registrationRequest.getEmail());
            users.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            users.setRole(registrationRequest.getRole());
            Users userResult = userRepo.save(users);

            if(userResult != null && userResult.getId() > 0){
                resp.setUsers(userResult);
                resp.setMessage("User saved successfully");
                resp.setStatusCode(200);
            } else {
                resp.setStatusCode(500);
                resp.setMessage("Failed to save user");
            }

        } catch (Exception e){
//            logger.error("Error during sign up: ", e);  // Improved logging
            resp.setStatusCode(500);
            resp.setMessage("An error occurred during registration");
        }
        return resp;
    }

    public ReqRes signIn(ReqRes signInRequest){
        ReqRes response = new ReqRes();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));
            var user = userRepo.findByEmail(signInRequest.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            String jwt = jwtUtils.generateToken(user);
            String refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);

            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Signed In");

        } catch (UsernameNotFoundException e) {
//            logger.warn("User not found: ", e);  // Improved logging
            System.out.println("Exception found"+ e.getMessage());
            response.setStatusCode(404);
            response.setMessage("Invalid credentials");

        } catch (Exception e){
//            logger.error("Error during sign in: ", e);  // Improved logging
            System.out.println("Error during sign in: "+ e.getMessage());
            response.setStatusCode(500);
            response.setMessage("An error occurred during sign in");
        }
        return response;
    }

    public ReqRes refreshToken(ReqRes refreshTokenRequest){
        ReqRes response = new ReqRes();

        try {
            String email = jwtUtils.extractUsername(refreshTokenRequest.getRefreshToken());
            Users users = userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if(jwtUtils.isTokenValid(refreshTokenRequest.getToken(), users)) {
                String jwt = jwtUtils.generateToken(users);
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenRequest.getRefreshToken());
                response.setExpirationTime("24Hr");
                response.setMessage("Successfully Refreshed Token");
            } else {
                response.setStatusCode(401);
                response.setMessage("Invalid or expired token");
            }

        } catch (UsernameNotFoundException e) {
//            logger.warn("User not found: ", e);  // Improved logging
            response.setStatusCode(404);
            response.setMessage("Invalid credentials");

        } catch (Exception e){
//            logger.error("Error during token refresh: ", e);  // Improved logging
            response.setStatusCode(500);
            response.setMessage("An error occurred during token refresh");
        }

        return response;
    }

}


//==================================default one=========================================================================

//package com.codewithsm.springsecuritywithjwtauth.service;
//
//import com.codewithsm.springsecuritywithjwtauth.dto.ReqRes;
//import com.codewithsm.springsecuritywithjwtauth.model.Users;
//import com.codewithsm.springsecuritywithjwtauth.repository.UserRepo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//
//@Service
//public class AuthService {
//
//    @Autowired
//    private UserRepo userRepo;
//    @Autowired
//    private JWTUtils jwtUtils;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    public ReqRes signUp(ReqRes registrationRequest){
//        ReqRes resp = new ReqRes();
//
//        try {
//            Users users = new Users();
//            users.setEmail(registrationRequest.getEmail());
//            users.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
//            users.setRole(registrationRequest.getRole());
//            Users userResult = userRepo.save(users);
//
//            if(userResult != null && userResult.getId() > 0){
//                resp.setUsers(userResult);
//                resp.setMessage("User saved successfully");
//                resp.setStatusCode(200);
//            }
//
//        }catch (Exception e){
//            resp.setStatusCode(500);
//            resp.setMessage(e.getMessage());
//        }
//        return resp;
//    }
//
//    public ReqRes signIn(ReqRes signInRequest){
//        ReqRes response = new ReqRes();
//
//        try {
//
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(),signInRequest.getPassword()));
//            var user = userRepo.findByEmail(signInRequest.getEmail()).orElseThrow();
//            System.out.println("User is "+ user);
//            var jwt = jwtUtils.generateToken(user);
//            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
//            response.setStatusCode(200);
//            response.setToken(jwt);
//            response.setRefreshToken(refreshToken);
//            response.setExpirationTime("24Hr");
//            response.setMessage("Successfully Signed In");
//
//        } catch (Exception e){
//            response.setStatusCode(500);
//            response.setMessage(e.getMessage());
//        }
//        return response;
//    }
//
//    public ReqRes refreshToken(ReqRes refreshTokenRequest){
//        ReqRes response = new ReqRes();
//        String email = jwtUtils.extractUsername(refreshTokenRequest.getRefreshToken());
//        Users users = userRepo.findByEmail(email).orElseThrow();
//        if(jwtUtils.isTokenValid(refreshTokenRequest.getToken(), users)) {
//            var jwt = jwtUtils.generateToken(users);
//            response.setStatusCode(200);
//            response.setToken(jwt);
//            response.setRefreshToken(refreshTokenRequest.getToken());
//            response.setExpirationTime("24Hr");
//            response.setMessage("Successfully Refreshed Token");
//        }
//        response.setStatusCode(500);
//        return response;
//    }
//
//}
