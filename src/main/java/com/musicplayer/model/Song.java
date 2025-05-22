package com.musicplayer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Song {
    private Long id;
    private String title;
    private String artist;
    private String album;
    private Integer duration;
    private String filePath;      // 对应数据库中的 file_path
    private String fileName;      // 对应数据库中的 file_name
    private String fileType;      // 对应数据库中的 file_type
    private Long fileSize;        // 对应数据库中的 file_size
    private LocalDateTime uploadTime;  // 对应数据库中的 upload_time
    private String genre;         // 对应数据库中的 genre
    private String coverImagePath;  // 对应数据库中的 cover_image_path
    private LocalDateTime createdAt;  // 对应数据库中的 created_at
    private LocalDateTime updatedAt;  // 对应数据库中的 updated_at
} 