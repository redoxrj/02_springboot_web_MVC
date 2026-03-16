package com.example.springbootweb.springbootweb.controllers;

import com.example.springbootweb.springbootweb.dto.LoginDto;
import com.example.springbootweb.springbootweb.dto.LoginResponseDto;
import com.example.springbootweb.springbootweb.dto.SignUpDto;
import com.example.springbootweb.springbootweb.dto.UserDto;
import com.example.springbootweb.springbootweb.services.AuthService;
import com.example.springbootweb.springbootweb.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor //only creates constructor for:final fields ,or fields marked with @NonNull
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup (@RequestBody SignUpDto signUpDto){

        UserDto userDto = userService.signUpUser(signUpDto);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);

    }

    @PostMapping("/login")
    public  ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response){
        LoginResponseDto loginResponseDto = authService.login(loginDto);

        Cookie cookie = new Cookie("refreshToken" ,loginResponseDto.getRefreshToken() );
        cookie.setHttpOnly(true);
//        cookie.setSecure(true); // more secure as it will only supports https domain , so for production only

        response.addCookie(cookie);
        return new ResponseEntity<>(loginResponseDto,HttpStatus.OK);

    }

    @PutMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(HttpServletRequest request){
//        request.getCookies()--> this returns an array of all cookies present so we can use for loop to iterate or use stream with filter method
      String refreshToken=  Arrays.stream(request.getCookies()) // will get convrted in stream
                .filter(cookie -> cookie.getName().equals("refreshToken"))
                .findFirst()
              .map(cookie -> cookie.getValue())
                .orElseThrow(()-> new AuthenticationException("refresh Token not found in the cookie") {
                });

        LoginResponseDto loginResponseDto = authService.refreshToken(refreshToken);

        return new ResponseEntity<>(loginResponseDto,HttpStatus.OK);



    }

}

// NOTE : controller ke method mein humesha public ResponseEntity<LoginResponseDto>  login  use krke method banana pdta hai while on rest things like services ke methods mein only public LoginResponseDto login krke banana hota hai as in controller we r actually giving response back to cleint.
