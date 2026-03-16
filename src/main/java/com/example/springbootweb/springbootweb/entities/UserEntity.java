package com.example.springbootweb.springbootweb.entities;

import com.example.springbootweb.springbootweb.entities.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@ToString
@Builder
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String name;

    private String password;

//    @Enumerated(EnumType.STRING) // now aisitize String value else number will be stored in table in case of by default EnumType.ORDINAL
//    private Role role; // for single role based
    @ElementCollection(fetch = FetchType.EAGER) // @ElementCollection coz since we are storing roles as a Set,(fetch = FetchType.EAGER) coz we want to fetch the roles at the same time directly when UserEntity is fetching not roles at later on or lazily.
     @Enumerated(EnumType.STRING)
    private Set<Role> roles; // for mutilples roles same user
    //NOTE : @ElementCollection tells Hibernate/JPA: "This field(private Set<Role> roles;) is a collection of values that are NOT entities, so store them in a separate table." Since Role is just an enum, not an entity, Hibernate stores it in another table.

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of();
        return roles.stream()
                .map((role)->new SimpleGrantedAuthority("ROLE_" +role.name()))
                .collect(Collectors.toSet());
        // now everytime we are storing user roles in context holder, also storing authorities related to that user as well.
    }

    @Override
    public String getUsername() {
        return this.email;// in the context of spring security,username is email
    }
}

/*
// NOTE :  UserDetails is an in-built spring security interfce(core component of spring security) that conatains user related information.
It represents a User in spring security.
It provides methods to get user information such as username,password and authorities(roles).
we generally use it to extend UserEntity.

 */