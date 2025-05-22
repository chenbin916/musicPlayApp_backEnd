package com.musicplayer.service.impl;

import com.musicplayer.model.Song;
import com.musicplayer.mapper.SongMapper;
import com.musicplayer.service.SongService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SongServiceImpl implements SongService {

    private final SongMapper songMapper;
    private final Path uploadDir;

    public SongServiceImpl(SongMapper songMapper) {
        this.songMapper = songMapper;
        this.uploadDir = Paths.get("uploads").toAbsolutePath().normalize();
        try {
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    @Override
    public List<Song> findAll() {
        return songMapper.findAll();
    }

    @Override
    public Song findById(Long id) {
        return songMapper.findById(id);
    }

    @Override
    public List<Song> findByTitle(String title) {
        return songMapper.findByTitle(title);
    }

    @Override
    public List<Song> findByArtist(String artist) {
        return songMapper.findByArtist(artist);
    }

    @Override
    public List<Song> findByGenre(String genre) {
        return songMapper.findByGenre(genre);
    }

    @Override
    @Transactional
    public Song create(Song song) {
        LocalDateTime now = LocalDateTime.now();
        song.setCreatedAt(now);
        song.setUpdatedAt(now);
        song.setUploadTime(now);
        songMapper.insert(song);
        return song;
    }

    @Override
    @Transactional
    public Song update(Song song) {
        song.setUpdatedAt(LocalDateTime.now());
        songMapper.update(song);
        return song;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Song song = findById(id);
        if (song != null) {
            deleteSongFiles(song);
            songMapper.delete(id);
        }
    }

    @Override
    public List<Song> searchSongs(String query) {
        return songMapper.findByTitle(query);
    }

    @Override
    @Transactional
    public Song createSong(Song song, MultipartFile audioFile, MultipartFile coverImage) {
        try {
            if (audioFile != null && !audioFile.isEmpty()) {
                String audioFileName = UUID.randomUUID().toString() + "_" + audioFile.getOriginalFilename();
                Path audioFilePath = uploadDir.resolve(audioFileName);
                Files.copy(audioFile.getInputStream(), audioFilePath);
                song.setFilePath(audioFilePath.toString());
                song.setFileName(audioFile.getOriginalFilename());
                song.setFileType(audioFile.getContentType());
                song.setFileSize(audioFile.getSize());
            }

            if (coverImage != null && !coverImage.isEmpty()) {
                String imageFileName = UUID.randomUUID().toString() + "_" + coverImage.getOriginalFilename();
                Path imageFilePath = uploadDir.resolve(imageFileName);
                Files.copy(coverImage.getInputStream(), imageFilePath);
                song.setCoverImagePath(imageFilePath.toString());
            }

            LocalDateTime now = LocalDateTime.now();
            song.setCreatedAt(now);
            song.setUpdatedAt(now);
            song.setUploadTime(now);
            
            songMapper.insert(song);
            return song;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @Override
    @Transactional
    public Song updateSong(Long id, Song song, MultipartFile audioFile, MultipartFile coverImage) {
        Song existingSong = findById(id);
        if (existingSong == null) {
            throw new RuntimeException("Song not found");
        }
        
        try {
            if (audioFile != null && !audioFile.isEmpty()) {
                deleteFileIfExists(existingSong.getFilePath());
                String audioFileName = UUID.randomUUID().toString() + "_" + audioFile.getOriginalFilename();
                Path audioFilePath = uploadDir.resolve(audioFileName);
                Files.copy(audioFile.getInputStream(), audioFilePath);
                song.setFilePath(audioFilePath.toString());
                song.setFileName(audioFile.getOriginalFilename());
                song.setFileType(audioFile.getContentType());
                song.setFileSize(audioFile.getSize());
            }

            if (coverImage != null && !coverImage.isEmpty()) {
                deleteFileIfExists(existingSong.getCoverImagePath());
                String imageFileName = UUID.randomUUID().toString() + "_" + coverImage.getOriginalFilename();
                Path imageFilePath = uploadDir.resolve(imageFileName);
                Files.copy(coverImage.getInputStream(), imageFilePath);
                song.setCoverImagePath(imageFilePath.toString());
            }

            song.setId(id);
            song.setUpdatedAt(LocalDateTime.now());
            songMapper.update(song);
            return song;
        } catch (IOException e) {
            throw new RuntimeException("Failed to update file", e);
        }
    }

    @Override
    @Transactional
    public void deleteSong(Long id) {
        Song song = findById(id);
        if (song != null) {
            deleteSongFiles(song);
            songMapper.delete(id);
        }
    }

    @Override
    @Transactional
    public Song uploadSong(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            // 从原始文件名中解析歌名和艺术家
            String originalFileName = file.getOriginalFilename();
            String fileNameWithoutExt = originalFileName.replaceFirst("[.][^.]+$", "");
            String[] parts = fileNameWithoutExt.split("-", 2);
            
            String title = parts[0].trim();
            String artist = parts.length > 1 ? parts[1].trim() : "未知艺术家";

            Song song = new Song();
            song.setTitle(title);
            song.setArtist(artist);
            song.setFilePath(filePath.toString());
            song.setFileName(file.getOriginalFilename());
            song.setFileType(file.getContentType());
            song.setFileSize(file.getSize());
            
            LocalDateTime now = LocalDateTime.now();
            song.setCreatedAt(now);
            song.setUpdatedAt(now);
            song.setUploadTime(now);

            songMapper.insert(song);
            return song;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        songMapper.delete(id);
    }

    @Override
    public Song save(MultipartFile file, String title, String artist, MultipartFile coverImage) {

        Song song = new Song();
        song.setTitle(title);
        song.setArtist(artist);
        //to do
        song.setFilePath(null);
        song.setFileName(file.getOriginalFilename());
        song.setFileType(file.getContentType());
        song.setFileSize(file.getSize());
        songMapper.insert(song);
        return song;
    }

    private void deleteSongFiles(Song song) {
        deleteFileIfExists(song.getFilePath());
        deleteFileIfExists(song.getCoverImagePath());
    }

    private void deleteFileIfExists(String filePath) {
        if (filePath != null) {
            try {
                Files.deleteIfExists(Paths.get(filePath));
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete file: " + filePath, e);
            }
        }
    }
} 