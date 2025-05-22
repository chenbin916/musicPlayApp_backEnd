package com.musicplayer.service;

import com.musicplayer.model.Song;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface SongService {
    List<Song> findAll();
    Song findById(Long id);
    List<Song> findByTitle(String title);
    List<Song> findByArtist(String artist);
    List<Song> findByGenre(String genre);
    Song create(Song song);
    Song update(Song song);
    void delete(Long id);
    List<Song> searchSongs(String query);
    Song createSong(Song song, MultipartFile audioFile, MultipartFile coverImage);
    Song updateSong(Long id, Song song, MultipartFile audioFile, MultipartFile coverImage);
    void deleteSong(Long id);
    Song uploadSong(MultipartFile file);
    void deleteById(Long id);
    Song save(MultipartFile file, String title, String artist, MultipartFile coverImage);
} 