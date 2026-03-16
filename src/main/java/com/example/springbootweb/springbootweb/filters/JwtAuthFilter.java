package com.example.springbootweb.springbootweb.filters;

import com.example.springbootweb.springbootweb.entities.UserEntity;
import com.example.springbootweb.springbootweb.services.JwtService;
import com.example.springbootweb.springbootweb.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
//JwtAuthFilter here is a custom Spring Security filter that Intercepts every HTTP request-->Extracts JWT from Authorization header-->Validates the token-->Loads the user-->Sets authentication in Spring Security context
    //(Client → Request with JWT → JwtAuthFilter → Validate → Set Authentication → Controller)
    //After this → Spring Security considers the user logged in
    //OncePerRequestFilter ensures The filter runs only once per request
    private final JwtService jwtService;
    private final UserService userService;

    @Autowired
    @Qualifier("handlerExceptionResolver") // to make it distinct bean(to prevent duplicate bean of same name)
    private HandlerExceptionResolver handlerExceptionResolver; // as current context is before/out of servlet dipatcher hence global excetion handler won't work hence have to use HandlerExceptionResolver

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
try {
    final String requestTokenHeader = request.getHeader("Authorization"); //Extract Authorization Header from client
    if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer")) {
        //if not present,skip authentication
        filterChain.doFilter(request, response);//"Go to next filter"
        return;
    }

    String token = requestTokenHeader.split("Bearer ")[1];
    Long userId = jwtService.getUserIdFromToken(token);

    if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) { //&& Check If Already Authenticated -->No need to set it again(Prevents duplicate authentication)
        UserEntity user = userService.getUserById(userId); //Load User from Database
        //Create Authentication Object below , this means Principal = user,Credentials = null (we already verified token),Authorities = null (as of now)
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, null);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); //Attach Request Details,Adds:IP address,Session ID
        SecurityContextHolder.getContext().setAuthentication(authenticationToken); //Set Authentication in Security Context holder,Now Spring Security knows This request belongs to this authenticated user.

    }

    filterChain.doFilter(request, response); // pass on to next filter just like next() in express.js for next middleware
    // we have to call this even the user not found otherwise your request will not go to next filter in filterchain (stuck here itself,Request will never reach controller.)

    // now we can do below anywhere in the app to get the user details at any moment of time :
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        UserEntity user = (UserEntity) auth.getPrincipal();

} catch (Exception e) {
    handlerExceptionResolver.resolveException(request,response,null,e);
}


    }
}
