package com.example.springbootweb.springbootweb;

import com.example.springbootweb.springbootweb.entities.UserEntity;
import com.example.springbootweb.springbootweb.services.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//@SpringBootTest //Because @SpringBootTest loads full application context including JPA. Since database configuration was missing/unavailable, Hibernate couldn't determine dialect and ApplicationContext failed to start.
@ExtendWith(SpringExtension.class)
@Import(
        JwtService.class)  //This loads ONLY JwtService, not full app.
@TestPropertySource(properties = {
        "jwt.secret.key=this_is_my_super_secure_secret_key_which_is_long_enough_123455555555555555555555555555555"
})
class SpringbootwebApplicationTests {

    @Autowired
    JwtService jwtService;

	@Test
	void contextLoads() {
	}

    @Test
    void testJwt(){
//        UserEntity userEntity = new UserEntity(1212L,"rjrajnish1729@gmail.com","raju","abc@123");
//        String token = jwtService.generateAccessToken(userEntity);
//        System.out.println(token);
//
//        Long userId = jwtService.getUserIdFromToken(token);
//        System.out.println(userId);

    }

}


// NOTE :
/*
When you use: @SpringBootTest --> You get full Spring Boot magic.
When you use: @ExtendWith + @Import -->You only get basic Spring context — no Boot auto-configuration.So property resolution does not work unless explicitly configured(@TestPropertySource).
 */