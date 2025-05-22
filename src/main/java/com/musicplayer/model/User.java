package com.musicplayer.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role;
    private LocalDateTime createdAt;  // 对应数据库中的 created_at
    private LocalDateTime updatedAt;  // 对应数据库中的 updated_at
} 