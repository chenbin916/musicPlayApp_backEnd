package com.musicplayer.controller;

import com.musicplayer.model.Song;
import com.musicplayer.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/songs")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3001"}, allowCredentials = "true")
public class SongController {

    private static final Logger logger = LoggerFactory.getLogger(SongController.class);

    @Autowired
    private SongService songService;

    @GetMapping
    public ResponseEntity<List<Song>> getAllSongs() {
        try {
            return ResponseEntity.ok(songService.findAll());
        } catch (Exception e) {
            logger.error("Error getting all songs", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable Long id) {
        try {
            Song song = songService.findById(id);
            if (song == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(song);
        } catch (Exception e) {
            logger.error("Error getting song by id: " + id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}/file")
    public ResponseEntity<?> getSongFile(@PathVariable Long id) {
        try {
            Song song = songService.findById(id);
            if (song == null || song.getFilePath() == null) {
                logger.error("Song or file path not found for id: " + id);
                return ResponseEntity.notFound().build();
            }

            Path filePath = Paths.get(song.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                logger.error("File not found or not readable: " + filePath);
                return ResponseEntity.notFound().build();
            }

            String contentType = song.getFileType();
            if (contentType == null || contentType.isEmpty()) {
                contentType = "audio/mpeg";
            }

            logger.info("Serving file: " + filePath + " with content type: " + contentType);
            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + song.getFileName() + "\"")
                .body(resource);
        } catch (Exception e) {
            logger.error("Error serving file for song id: " + id, e);
            return ResponseEntity.internalServerError().body("Error serving file");
        }
    }

    @PostMapping
    public ResponseEntity<Song> createSong(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("artist") String artist,
            @RequestParam(value = "coverImage", required = false) MultipartFile coverImage) {
        try {
            Song song = songService.save(file, title, artist, coverImage);
            return ResponseEntity.ok(song);
        } catch (Exception e) {
            logger.error("Error creating song", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        try {
            songService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error deleting song with id: " + id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Song>> searchSongs(@RequestParam String query) {
        try {
            return ResponseEntity.ok(songService.searchSongs(query));
        } catch (Exception e) {
            logger.error("Error searching songs with query: " + query, e);
            return ResponseEntity.internalServerError().build();
        }
    }
    @PostMapping("/upload")
    public ResponseEntity<?> uploadSong(@RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(songService.uploadSong(file));
        } catch (Exception e) {
            logger.error("Error uploading song", e);
            return ResponseEntity.badRequest().body("Error uploading song");
        }
    }

} 