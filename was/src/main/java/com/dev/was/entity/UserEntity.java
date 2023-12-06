package com.dev.was.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name="tm_user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String name;
    private String password;
    @Column(nullable = false, unique = true)
    private String email;
    private String phone;
    private String profileImg;
    private String role; //유저 권한 (일반 유저, 관리자)

    @Builder
    public UserEntity(Long id, String userId, String name, String password, String email, String phone, String profileImg, String role) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.profileImg = profileImg;
        this.role = role;
    }
}