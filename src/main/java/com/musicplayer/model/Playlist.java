package com.musicplayer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Playlist {
    private Long id;
    private String name;
    private String description;    // 对应数据库中的 description
    private Long userId;          // 对应数据库中的 user_id
    private List<Song> songs;     // 关联字段，对应 playlist_songs 关联表
    private LocalDateTime createdAt;  // 对应数据库中的 created_at
    private LocalDateTime updatedAt;  // 对应数据库中的 updated_at
} 