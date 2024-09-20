package com.akhi.trading_application.controller;

import java.io.Console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akhi.trading_application.config.JwtProvider;
import com.akhi.trading_application.modal.User;
import com.akhi.trading_application.repository.UserRepository;
import com.akhi.trading_application.response.AuthResponse;
import com.akhi.trading_application.service.CustomUserDetailsService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) throws Exception{

        User existedUser = userRepository.findByEmail(user.getEmail());
        
        if (existedUser != null) {
            System.out.println("User exists");
            throw new Exception("User already exists");
        }
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setFullName(user.getFullName());

        User savedUser = userRepository.save(newUser);

        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt=JwtProvider.generateToken(auth);

        AuthResponse response = new AuthResponse();
        response.setJwt(jwt);
        response.setStatus(true);
        response.setMessage("Registration Successfull.....!");
        return new ResponseEntity<>(response , HttpStatus.CREATED);

    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) throws Exception{

        String userName=user.getEmail();
        String password=user.getPassword();
        

        Authentication auth = authenticate(userName,password);
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt=JwtProvider.generateToken(auth);

        AuthResponse response = new AuthResponse();
        response.setJwt(jwt);
        response.setStatus(true);
        response.setMessage("login Successfull.....!");
        return new ResponseEntity<>(response , HttpStatus.CREATED);
    }

    private Authentication authenticate(String userName, String password) throws Exception {
        UserDetails userDetais=customUserDetailsService.loadUserByUsername(userName);
        if(userDetais==null){
            throw new BadCredentialsException("Invalid Username....!");
        }
        if(!password.equals(userDetais.getPassword())){
            throw new BadCredentialsException("Invalid Password....!");
        }

        return new UsernamePasswordAuthenticationToken(userDetais, password, userDetais.getAuthorities());

    }
        


}
