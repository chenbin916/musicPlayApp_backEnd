package com.musicplayer.mapper;

import com.musicplayer.model.Song;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface SongMapper {
    List<Song> findAll();
    Song findById(Long id);
    List<Song> findByTitle(String title);
    List<Song> findByArtist(String artist);
    List<Song> findByGenre(String genre);
    int insert(Song song);
    int update(Song song);
    int delete(Long id);
} 