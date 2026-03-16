package com.example.springbootweb.springbootweb.configs;

import com.example.springbootweb.springbootweb.filters.JwtAuthFilter;
import com.example.springbootweb.springbootweb.handlers.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import static com.example.springbootweb.springbootweb.entities.enums.Role.ADMIN;

@Configuration//Tells Spring this class contains bean definitions(Spring will load it at startup)
@EnableWebSecurity //Enables Spring Security and tells Spring Boot: “I want to customize the default security configuration.” (without this customiztion by default : All endpoints secured,Default login form,Basic authentication)
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private static final String[] publicRoues ={
            "/posts","/error","/auth/**","/home.html"

    };
    //NOTE : roles --> given to a group of users while authorities are specific permissions(in high level bothe are same for spring security).

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(auth->
                        auth
                                .requestMatchers(publicRoues).permitAll()
                                //Public Endpoints (No Login/auth Required)
//                                .requestMatchers("/employees/**").hasRole(ADMIN.name())
                                .requestMatchers(HttpMethod.GET,"/employees/**").permitAll()
                                .requestMatchers(HttpMethod.POST,"/employees/**").hasRole(ADMIN.name())
//                                .requestMatchers("/posts/**").hasAnyRole("ADMIN") //(Role-Based Access) Requires role:ROLE_ADMIN ,Spring automatically adds ROLE_ prefix.
                                .anyRequest().authenticated())  // All other requests require authentication

                .csrf(csrfConfig -> csrfConfig.disable())  // Disable CSRF for simplicity, Not recommended for production unless using JWT(STATELESS) properly.

                .sessionManagement(sessionConfig ->
                        sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )   // Disable JSESSIONID for simplicity, "Do NOT create HTTP sessions (JSESSIONID)."
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) //add jwtAuthFilter just before UsernamePasswordAuthenticationFilter So by the time Spring reaches authentication filter,the user is already authenticated and just continue the chain(i.e will then the request go to the DispatchServlet & controller)

                .oauth2Login(oauth2config->oauth2config
                        .failureUrl("/login?error=true")
                        .successHandler(oAuth2SuccessHandler)
                )

//                .formLogin(Customizer.withDefaults())  // commented coz we r not using Form login/Session login instead JWT login so form login is unnecessary

//                .logout(Customizer.withDefaults()) // same
                 ;

        return httpSecurity.build();   //This builds the entire security configuration.
    }

    // NOTE : SecurityFilterChain Bean above -->This is the core of Spring Security 6 configuration. Spring Security works internally using a filter chain (a series of filters that intercept HTTP requests).
    //You are customizing that filter chain here.
    //NOTE : as per above /employees is public but rest types /employees/2 is authenticated.

//    // Custom Authentication Providers
//    @Bean
//    UserDetailsService myInMemoryUserDetailsService(){
//
//        UserDetails user = User  //for user role
//                .withUsername("<UserName>")  // Replace <UserName> with the actual username
//                .password(passwordEncoder().encode("<UserPassword>"))   // Replace <UserPassword> with the actual password
//                .roles("USER")
//                .build();
//
//        UserDetails admin = User      //for admin role
//                .withUsername("<AdminName>") // Replace <AdminName> with the actual username
//                .password(passwordEncoder().encode("<AdminPassword>"))  // Replace <AdminPassword> with the actual password
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(user,admin);
//
//    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();

    }


}