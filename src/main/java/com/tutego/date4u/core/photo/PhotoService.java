package com.tutego.date4u.core.photo;

import java.io.UncheckedIOException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tutego.date4u.core.FileSystem;

@Service
public class PhotoService {
    private final FileSystem fs;
    private final AwtBicubicThumbnail thumbnail;

    public PhotoService(FileSystem fs, AwtBicubicThumbnail thumbnail) {
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
}
