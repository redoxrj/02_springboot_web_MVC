package com.example.springbootweb.springbootweb.dto;

import com.example.springbootweb.springbootweb.entities.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto { // to be accept in request body by controller
    @NotBlank
    private String name;

    @NotBlank
    private String email;
    @NotBlank
    private String password;

    @NotEmpty
    private Set<Role> roles;
}
