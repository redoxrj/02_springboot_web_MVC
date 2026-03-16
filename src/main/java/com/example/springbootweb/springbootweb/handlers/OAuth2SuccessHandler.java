package com.example.springbootweb.springbootweb.handlers;

import com.example.springbootweb.springbootweb.entities.UserEntity;
import com.example.springbootweb.springbootweb.services.JwtService;
import com.example.springbootweb.springbootweb.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //converting authentication object to OAuth2AuthenticationToken
        OAuth2AuthenticationToken token= (OAuth2AuthenticationToken) authentication;
        DefaultOAuth2User oAuth2User= (DefaultOAuth2User) token.getPrincipal();

        log.info(oAuth2User.toString());
        log.info(oAuth2User.getAttribute("email"));

        String email = oAuth2User.getAttribute("email");

         UserEntity user= userService.getUserByEmail(email);

         if(user==null){
             UserEntity newUser= UserEntity.builder()
                     .name(oAuth2User.getAttribute("name"))
                     .email(oAuth2User.getAttribute("email"))
                     .build();
              user = userService.save(newUser);
         }
        String accessToken= jwtService.generateAccessToken(user);
        String refreshToken= jwtService.generateRefreshToken(user);

        Cookie cookie = new Cookie("refreshToken" ,refreshToken );
        cookie.setHttpOnly(true);
//        cookie.setSecure(deployEnv.equals("prod"));
        response.addCookie(cookie);

        String frontEndUrl = "http://localhost:8080/home.html?token="+accessToken; // passing access Token back to the client
        getRedirectStrategy().sendRedirect(request,response,frontEndUrl);




    }

}
