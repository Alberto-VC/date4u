package com.tutego.date4u.core;

import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileSystem {
    private final Path root = Paths.get(System.getProperty("user.home")).resolve("fs");

    public FileSystem() {
        try {
            if (!Files.isDirectory(root)) Files.createDirectories(root);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public long getFreeDiskSpace() {
        return root.toFile().getFreeSpace();
    }

    public byte[] load(String filename) {
        try {
            return Files.readAllBytes(root.resolve(filename));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void store(String filename, byte[] data) {
        try {
            Files.write(root.resolve(filename), data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
