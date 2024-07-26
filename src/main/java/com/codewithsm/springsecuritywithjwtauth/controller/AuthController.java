package com.codewithsm.springsecuritywithjwtauth.controller;

import com.codewithsm.springsecuritywithjwtauth.dto.ReqRes;
import com.codewithsm.springsecuritywithjwtauth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<ReqRes> signUp(@RequestBody ReqRes signUpRequest){
        ReqRes response = authService.signUp(signUpRequest);
        if(response.getStatusCode() == 200){
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<ReqRes> signIn(@RequestBody ReqRes signInRequest){
        ReqRes response = authService.signIn(signInRequest);
        if(response.getStatusCode() == 200){
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes refreshTokenRequest){
        ReqRes response = authService.refreshToken(refreshTokenRequest);
        if(response.getStatusCode() == 200){
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
    }
}
