package com.rk.service;

import com.rk.Exception.SpringStoreException;
import com.rk.dto.RegisterRequest;
import com.rk.model.NotificationMail;
import com.rk.model.User;
import com.rk.model.VerificationToken;
import com.rk.repository.UserRepository;
import com.rk.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final VerificationTokenRepository verificationTokenRepository;

    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    @Transactional
    public void signup(RegisterRequest registerRequest){
        User user=new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encodePassword(registerRequest.getPassword()));
        user.setEnabled(false);
        userRepository.save(user);

        String token = generateVerificationToken(user);
        String message = mailContentBuilder.build("Thank you for signing up to Spring Store, " +
                "please click on the below url to activate your account : "
                + "http://localhost:8080/api/auth/accountVerification/" + token);

        mailService.sendMail(new NotificationMail("Please Activate your account", user.getEmail(), message));
        log.info("User registration success");
    }

    private String generateVerificationToken(User user) {
        String token= UUID.randomUUID().toString();
        VerificationToken verificationToken=new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);

        return token;
    }


    public void verifyAccount(String token) {
        Optional<VerificationToken> vertoken=verificationTokenRepository.findByToken(token);
        fetchUserAndEnable(vertoken.orElseThrow(()->new SpringStoreException("Invalid Token")));
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username=verificationToken.getUser().getUsername();
        User user=userRepository.findByUsername(username).orElseThrow(()->new SpringStoreException("Username not found"));
        user.setEnabled(true);
        userRepository.save(user);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }


}
