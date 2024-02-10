package com.tutego.date4u.interfaces.shell;

import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import com.tutego.date4u.core.photo.PhotoService;

@ShellComponent
public class PhotoCommands {
    private final PhotoService photoService;

    public PhotoCommands(PhotoService photoService) {
        this.photoService = photoService;
    }

    @ShellMethod("Show Photo")
    String showPhoto(String name) {
        return photoService.download(name).map(bytes -> {
            try {
                var image = ImageIO.read(new ByteArrayInputStream(bytes));
                return "Width: " + image.getWidth()
                        + ", Height: " + image.getHeight();
            } catch (Exception e) {
                return e.getMessage();
            }
        }).orElse("Photo not found");
    }
}
