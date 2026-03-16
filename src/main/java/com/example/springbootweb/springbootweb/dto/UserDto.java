package com.example.springbootweb.springbootweb.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {  // to be return in response body by controller
    private Long id;
    private String name;
    private String email;
}
