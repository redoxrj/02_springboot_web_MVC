package com.example.springbootweb.springbootweb.services;

import com.example.springbootweb.springbootweb.dto.LoginDto;
import com.example.springbootweb.springbootweb.dto.LoginResponseDto;
import com.example.springbootweb.springbootweb.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@ToString
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final SessionService sessionService;

    public LoginResponseDto login (LoginDto loginDto){
        System.out.println("ff");
        Authentication authentication =authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword())
        );
        //NOTE : now it's duty of authenticationManager to authenticate the user and if found give user details
        System.out.println("authentication : "+authentication);
        UserEntity user = (UserEntity) authentication.getPrincipal();
        System.out.println("user : "+user);
        String accessToken= jwtService.generateAccessToken(user);
        String refreshToken= jwtService.generateRefreshToken(user);
        sessionService.addSession(user.getId(),refreshToken);

        return new LoginResponseDto(user.getId(),accessToken,refreshToken) ;

    }

    public LoginResponseDto refreshToken(String refreshToken) {
        Long userId=  jwtService.getUserIdFromToken(refreshToken); // make sure given refreshToken is valid ,not expired and fetch id(userId) from that token
        sessionService.validateRefreshTokenWithSession(refreshToken);
        UserEntity user = userService.getUserById(userId);
        String accessToken= jwtService.generateAccessToken(user);
        return new LoginResponseDto(user.getId(),accessToken,refreshToken) ;


    }
}
