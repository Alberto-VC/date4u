package com.tutego.date4u.core.photo;

import com.tutego.date4u.core.FileSystem;
import com.tutego.date4u.core.event.NewPhotoEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.UncheckedIOException;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PhotoService {
    private final FileSystem fs;

    @ThumbnailRendering(ThumbnailRendering.RenderQuality.FAST)
    private final Thumbnail thumbnail;

    //@Autowired
    //private ApplicationEventPublisher publisher;

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
        String imageName = UUID.randomUUID().toString();
        fs.store(imageName + ".jpg", imageBytes);

        byte[] thumbnailBytes = thumbnail.thumbnail(imageBytes);

        fs.store(imageName + "-thumb.jpg", thumbnailBytes);
        NewPhotoEvent newPhotoEvent = new NewPhotoEvent(imageName, OffsetDateTime.now());

        //publisher.publishEvent(newPhotoEvent);
        return imageName;
    }
}
