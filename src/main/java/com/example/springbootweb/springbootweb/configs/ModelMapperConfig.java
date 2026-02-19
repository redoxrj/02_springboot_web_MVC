package com.example.springbootweb.springbootweb.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    // factory method
  @Bean
    public ModelMapper  modelMapperBean(){
      return  new ModelMapper(); // object milega return mein
  }
  // this way Spring registers a ModelMapper bean in the container.

}
