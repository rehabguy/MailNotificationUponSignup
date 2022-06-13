package com.rk.controller;

import com.rk.dto.RegisterRequest;
import com.rk.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody RegisterRequest registerRequest){
        authService.signup(registerRequest);
        return new ResponseEntity("User Registration success", HttpStatus.OK);
    }

    @GetMapping("/accountVerification/{token}")
    public ResponseEntity accountVerify(@PathVariable String token){
        authService.verifyAccount(token);
        return new ResponseEntity("Account verified",HttpStatus.OK);
    }


}
