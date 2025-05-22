package com.musicplayer.exception;

public class PlaylistException extends BusinessException {
    public PlaylistException(String message) {
        super("PLAYLIST_ERROR", message);
    }

    public static PlaylistException notFound(Long playlistId) {
        return new PlaylistException("Playlist with id " + playlistId + " not found");
    }

    public static PlaylistException accessDenied(Long playlistId) {
        return new PlaylistException("Access denied to playlist with id " + playlistId);
    }

    public static PlaylistException songAlreadyExists(Long playlistId, Long songId) {
        return new PlaylistException("Song with id " + songId + " already exists in playlist " + playlistId);
    }

    public static PlaylistException songNotFound(Long playlistId, Long songId) {
        return new PlaylistException("Song with id " + songId + " not found in playlist " + playlistId);
    }

    public static PlaylistException invalidOperation(String operation) {
        return new PlaylistException("Invalid operation: " + operation);
    }
} 