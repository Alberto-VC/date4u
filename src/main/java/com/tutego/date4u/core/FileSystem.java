package com.tutego.date4u.core;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileSystem {
    private final Path root;

    public FileSystem(Path root) {
        this.root = root;
        try {
            if (!Files.isDirectory(root)) Files.createDirectories(root);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public FileSystem() {
        this(Paths.get(System.getProperty("user.home")).resolve("fs").toAbsolutePath().normalize());
    }

    public long getFreeDiskSpace() {
        return root.toFile().getFreeSpace();
    }

    public byte[] load(String filename) {
        try {
            Path path = resolve(filename);
            return Files.readAllBytes(root.resolve(filename));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void store(String filename, byte[] data) {
        try {
            Files.write(root.resolve(filename), data);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Path resolve(String filename) {
        Path path = root.resolve(filename).toAbsolutePath().normalize();
        if(!path.startsWith(root)) throw new SecurityException("Access to " + path + " denied");
        return path;
    }
}
