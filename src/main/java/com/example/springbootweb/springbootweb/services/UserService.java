package com.example.springbootweb.springbootweb.services;

import com.example.springbootweb.springbootweb.dto.SignUpDto;
import com.example.springbootweb.springbootweb.dto.UserDto;
import com.example.springbootweb.springbootweb.entities.UserEntity;
import com.example.springbootweb.springbootweb.exceptions.ResourceAlreadyExistsException;
import com.example.springbootweb.springbootweb.exceptions.ResourseNotFoundException;
import com.example.springbootweb.springbootweb.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(()->new BadCredentialsException("user not found with email : " + username));
    }

    public UserEntity getUserById(Long id){
        return userRepository.findById(id).orElseThrow(()-> new ResourseNotFoundException("user not found with id : " + id));

    }
    public UserEntity getUserByEmail(String email){
        return userRepository.findByEmail(email).orElse(null);

    }

    public UserDto signUpUser(SignUpDto signUpDto){

        Optional<UserEntity> userEntity= userRepository.findByEmail(signUpDto.getEmail());
        if(userEntity.isPresent()) throw new ResourceAlreadyExistsException("user already exists with email : "+signUpDto.getEmail());
        UserEntity userToBeCreated = modelMapper.map(signUpDto,UserEntity.class);
        userToBeCreated.setPassword(passwordEncoder.encode(userToBeCreated.getPassword()));
        UserEntity savedUser = userRepository.save(userToBeCreated);
        return modelMapper.map(savedUser,UserDto.class);


    }

    public UserEntity save(UserEntity newUser) {
        return userRepository.save(newUser);
    }
}

// NOTE : rcfr token --> state/session wise token while JWT token is stateless token

/*
// NOTE :  UserDetailsService is an in-built spring security interfce(core component of spring security) that is used by AuthenticationProviders to get the user details.
It has a single method :  loadUserByUsername .
purpose : to fetch user details from a datasource(eg-database) based on the username.
we generally use it to extend UserService.

UserDetailsService is an interface while InMemoryUserDetailsManager is spring security implementation of UserDetailsService that stores/fetch user info. in/from memory(InMemory)  and giving back to Providers.
purpose : to store user details in memory,typically for testing or small applications.You define users directly in the configuration(application.properties)

Jwt dont' require confidentiality(not an encryption like bcrypt password hash encode so that others cannot read if they get it),but in case of jwt token others can read the informartion of the jwt token if they somehow got the info.
But Jwt require integrity(not tempered in between) and authenticity(reliable/authentic source from one(cleint) to another(server) entity).

encryption(bcrypt) and encoding(jwt) are different.
jwt token 3rd part signature is always dependent on the payload, any temper in payload chnages the original signature.

 */
