package com.tutego.date4u.core.photo;

import com.tutego.date4u.core.FileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.UncheckedIOException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class PhotoService {
    Logger log = LoggerFactory.getLogger(PhotoService.class);

    private final FileSystem fs;

    @ThumbnailRendering(ThumbnailRendering.RenderQuality.FAST)
    private final Thumbnail thumbnail;

    public PhotoService(FileSystem fs, @Qualifier("fastThumbnailRenderer") Thumbnail thumbnail) {
        this.fs = fs;
        this.thumbnail = thumbnail;
    }

    @Cacheable(cacheNames = "date4u.filesystem.file",
            key = "#name",
            condition = "#name.length() > 3",
            unless = "#result == null")
    public Optional<byte[]> download(String name) {
        try {
            return Optional.of(fs.load(name + ".jpg"));
        } catch (UncheckedIOException e) {
            return Optional.empty();
        }
    }

    public String upload(byte[] imageBytes) {
        Future<byte[]> thumbnailBytes = thumbnail.thumbnail(imageBytes);

        String imageName = UUID.randomUUID().toString();

        fs.store(imageName + ".jpg", imageBytes);

        try{
            log.info("Thumbnail bytes: {}", thumbnailBytes.get());
            fs.store(imageName + "-thumb.jpg", thumbnailBytes.get());
        }catch (InterruptedException | ExecutionException e){
            throw new IllegalStateException(e);
        }

        return imageName;
    }
}
