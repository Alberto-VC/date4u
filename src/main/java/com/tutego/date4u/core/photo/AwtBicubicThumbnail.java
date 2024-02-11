package com.tutego.date4u.core.photo;

import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import java.awt.Graphics2D;

@Service
public class AwtBicubicThumbnail {
    private static BufferedImage create(BufferedImage source, int width, int height) {

        double thumbRatio = (double) width / height;
        double imageRatio = (double) source.getWidth() / source.getHeight();

        if (thumbRatio < imageRatio)
            height = (int) (width / imageRatio);
        else
            width = (int) (height * imageRatio);

        BufferedImage thumb = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = thumb.createGraphics();

        g2.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION,
                java.awt.RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.drawImage(source, 0, 0, width, height, null);
        g2.dispose();

        return thumb;
    }

    public byte[] thumbnail(byte[] imageBytes) {
        try (InputStream is = new ByteArrayInputStream(imageBytes);
                ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            BufferedImage thumbnail = create(ImageIO.read(is), 200, 200);
            ImageIO.write(thumbnail, "jpg", baos);

            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
