package com.tutego.date4u.core.photo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import javax.imageio.ImageIO;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import org.springframework.stereotype.Service;

@Service
public class AwtNearestNeighborThumbnail implements Thumbnail {
    private static BufferedImage create(BufferedImage source,
            int width, int height) {

        double thumbRatio = (double) width / (double) height;
        double imageRatio = (double) source.getWidth() / (double) source.getHeight();

        if (thumbRatio < imageRatio) {
            height = (int) (width / imageRatio);
        } else {
            width = (int) (height * imageRatio);
        }

        BufferedImage thumb = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        Graphics2D g2 = thumb.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2.drawImage(source, 0, 0, width, height, null);
        g2.dispose();

        return thumb;

    }

    @Override
    public byte[] thumbnail(byte[] imageBytes) {
        try (InputStream is = new ByteArrayInputStream(imageBytes);
                ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage thumbnail = create(ImageIO.read(is), 200, 200);
            ImageIO.write(thumbnail, "jpg", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
