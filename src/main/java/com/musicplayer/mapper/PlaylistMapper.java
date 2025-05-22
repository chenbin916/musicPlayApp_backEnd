package com.musicplayer.mapper;

import com.musicplayer.model.Playlist;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface PlaylistMapper {
    List<Playlist> findByUserId(Long userId);
    Playlist findById(Long id);
    int insert(Playlist playlist);
    int update(Playlist playlist);
    int delete(Long id);
    void addSongToPlaylist(Long playlistId, Long songId);
    void removeSongFromPlaylist(Long playlistId, Long songId);
    List<Playlist> findAll();
} 