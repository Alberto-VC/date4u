package com.tutego.date4u.core.photo;

import java.io.UncheckedIOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.tutego.date4u.core.FileSystem;

@Service
public class PhotoService {
    private final FileSystem fs;
    private final Thumbnail thumbnail;

    public PhotoService(FileSystem fs, Thumbnail thumbnail) {
        this.fs = fs;
        this.thumbnail = thumbnail;
    }

    public Optional<byte[]> download(String name) {
        try {
            return Optional.of(fs.load(name + ".jpg"));
        } catch (UncheckedIOException e) {
            return Optional.empty();
        }
    }

    public String upload(byte[] imageBytes) {
        String imageName = UUID.randomUUID().toString();
        fs.store(imageName + ".jpg", imageBytes);

        byte[] thumbnailBytes = thumbnail.thumbnail(imageBytes);

        fs.store(imageName + "-thumb.jpg", thumbnailBytes);
        return imageName;
    }
}
