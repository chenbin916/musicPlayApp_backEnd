package com.musicplayer.service;

import com.musicplayer.model.Playlist;
import java.util.List;

public interface PlaylistService {
    List<Playlist> findByUserId(Long userId);
    Playlist findById(Long id);
    Playlist create(Playlist playlist);
    Playlist update(Playlist playlist);
    void delete(Long id);
    void addSongToPlaylist(Long playlistId, Long songId);
    void removeSongFromPlaylist(Long playlistId, Long songId);
    List<Playlist>  findAll();

    void deleteById(Long id);

    int insert(Playlist playlist);
    Playlist save(Playlist playlist);
} 