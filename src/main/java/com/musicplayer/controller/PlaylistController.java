package com.musicplayer.controller;

import com.musicplayer.model.Playlist;
import com.musicplayer.service.PlaylistService;
import com.musicplayer.exception.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/playlists")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3001"}, allowCredentials = "true")
public class PlaylistController {

    private static final Logger logger = LoggerFactory.getLogger(PlaylistController.class);

    @Autowired
    private PlaylistService playlistService;

    @GetMapping
    public ResponseEntity<List<Playlist>> getAllPlaylists() {
        try {
            return ResponseEntity.ok(playlistService.findAll());
        } catch (Exception e) {
            logger.error("Error getting all playlists", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Playlist> getPlaylistById(@PathVariable Long id) {
        try {
            Playlist playlist = playlistService.findById(id);
            if (playlist == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(playlist);
        } catch (Exception e) {
            logger.error("Error getting playlist by id: " + id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<Playlist> createPlaylist(@RequestBody Playlist playlist) {
        try {
            return ResponseEntity.ok(playlistService.save(playlist));
        } catch (Exception e) {
            logger.error("Error creating playlist", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Playlist> updatePlaylist(@PathVariable Long id, @RequestBody Playlist playlist) {
        try {
            playlist.setId(id);
            return ResponseEntity.ok(playlistService.update(playlist));
        } catch (Exception e) {
            logger.error("Error updating playlist with id: " + id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable Long id) {
        try {
            playlistService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error deleting playlist with id: " + id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Playlist>> getPlaylistsByUserId(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(playlistService.findByUserId(userId));
        } catch (Exception e) {
            logger.error("Error getting playlists for user: " + userId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<Void> addSongToPlaylist(@PathVariable Long playlistId, @PathVariable Long songId) {
        try {
            playlistService.addSongToPlaylist(playlistId, songId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error adding song to playlist", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<Void> removeSongFromPlaylist(@PathVariable Long playlistId, @PathVariable Long songId) {
        try {
            playlistService.removeSongFromPlaylist(playlistId, songId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error removing song from playlist", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserException("User not authenticated");
        }
        
        Object principal = authentication.getPrincipal();
        if (principal instanceof org.springframework.security.core.userdetails.User) {
            org.springframework.security.core.userdetails.User userDetails = 
                (org.springframework.security.core.userdetails.User) principal;
            // 从UserDetails中获取用户ID
            // 这里假设用户ID存储在UserDetails的username字段中
            try {
                return Long.parseLong(userDetails.getUsername());
            } catch (NumberFormatException e) {
                throw new UserException("Invalid user ID format");
            }
        }
        throw new UserException("User not found");
    }
} 