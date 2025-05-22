package com.musicplayer.service.impl;

import com.musicplayer.model.Playlist;
import com.musicplayer.model.Song;
import com.musicplayer.mapper.PlaylistMapper;
import com.musicplayer.mapper.SongMapper;
import com.musicplayer.service.PlaylistService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistMapper playlistMapper;
    private final SongMapper songMapper;

    public PlaylistServiceImpl(PlaylistMapper playlistMapper, SongMapper songMapper) {
        this.playlistMapper = playlistMapper;
        this.songMapper = songMapper;
    }

    @Override
    public List<Playlist> findByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return playlistMapper.findByUserId(userId);
    }

    @Override
    public Playlist findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Playlist ID cannot be null");
        }
        Playlist playlist = playlistMapper.findById(id);
        if (playlist == null) {
            throw new RuntimeException("Playlist not found with id: " + id);
        }
        return playlist;
    }

    @Override
    @Transactional
    public Playlist create(Playlist playlist) {
        if (playlist == null) {
            throw new IllegalArgumentException("Playlist cannot be null");
        }
        if (playlist.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (playlist.getName() == null || playlist.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Playlist name cannot be empty");
        }

        LocalDateTime now = LocalDateTime.now();
        playlist.setCreatedAt(now);
        playlist.setUpdatedAt(now);
        playlistMapper.insert(playlist);
        return playlist;
    }

    @Override
    @Transactional
    public Playlist update(Playlist playlist) {
        if (playlist == null) {
            throw new IllegalArgumentException("Playlist cannot be null");
        }
        if (playlist.getId() == null) {
            throw new IllegalArgumentException("Playlist ID cannot be null");
        }
        if (playlist.getName() == null || playlist.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Playlist name cannot be empty");
        }

        // 验证播放列表是否存在
        Playlist existingPlaylist = findById(playlist.getId());
        if (!existingPlaylist.getUserId().equals(playlist.getUserId())) {
            throw new RuntimeException("Cannot update playlist: user ID mismatch");
        }

        playlist.setUpdatedAt(LocalDateTime.now());
        playlistMapper.update(playlist);
        return playlist;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Playlist ID cannot be null");
        }
        // 验证播放列表是否存在
        findById(id);
        playlistMapper.delete(id);
    }

    @Override
    @Transactional
    public void addSongToPlaylist(Long playlistId, Long songId) {
        if (playlistId == null) {
            throw new IllegalArgumentException("Playlist ID cannot be null");
        }
        if (songId == null) {
            throw new IllegalArgumentException("Song ID cannot be null");
        }

        // 验证播放列表和歌曲是否存在
        Playlist playlist = findById(playlistId);
        Song song = songMapper.findById(songId);
        if (song == null) {
            throw new RuntimeException("Song not found with id: " + songId);
        }

        // 检查歌曲是否已经在播放列表中
        List<Song> existingSongs = playlist.getSongs();
        if (existingSongs != null && existingSongs.stream().anyMatch(s -> s.getId().equals(songId))) {
            throw new RuntimeException("Song already exists in playlist");
        }

        playlistMapper.addSongToPlaylist(playlistId, songId);
    }

    @Override
    @Transactional
    public void removeSongFromPlaylist(Long playlistId, Long songId) {
        if (playlistId == null) {
            throw new IllegalArgumentException("Playlist ID cannot be null");
        }
        if (songId == null) {
            throw new IllegalArgumentException("Song ID cannot be null");
        }

        // 验证播放列表和歌曲是否存在
        Playlist playlist = findById(playlistId);
        Song song = songMapper.findById(songId);
        if (song == null) {
            throw new RuntimeException("Song not found with id: " + songId);
        }

        // 检查歌曲是否在播放列表中
        List<Song> existingSongs = playlist.getSongs();
        if (existingSongs == null || existingSongs.stream().noneMatch(s -> s.getId().equals(songId))) {
            throw new RuntimeException("Song not found in playlist");
        }

        playlistMapper.removeSongFromPlaylist(playlistId, songId);
    }

    @Override
    public List<Playlist> findAll() {
        return playlistMapper.findAll();
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Playlist ID cannot be null");
        }
        // 验证播放列表是否存在
        findById(id);
        playlistMapper.delete(id);
    }

    @Override
    public int insert(Playlist playlist) {
       return playlistMapper.insert(playlist);
    }

    @Override
    public Playlist save(Playlist playlist) {
        int id=playlistMapper.insert(playlist);
        playlist.setId((long) id);
        return playlist;
    }
} 