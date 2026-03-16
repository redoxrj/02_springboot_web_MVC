package com.example.springbootweb.springbootweb.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Table(name = "user_sessions")
public class SessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String refreshToken;

//    @ManyToOne  //ManySessionToOneuserId
    private Long userId;

    @CreationTimestamp
    private LocalDateTime lastUsedAt;
}
