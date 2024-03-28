package com.tutego.date4u.core.photo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.springframework.stereotype.Service;

@Service("qualityThumbnailRenderer")
@ThumbnailRendering(ThumbnailRendering.RenderQuality.QUALITY)
public class AwtBicubicThumbnail implements Thumbnail {
    private static BufferedImage create(BufferedImage source,
            int width, int height) {

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

    @Override
    public Future<byte[]> thumbnail(byte[] imageBytes) {
        try (InputStream is = new ByteArrayInputStream(imageBytes);
                ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            BufferedImage thumbnail = create(ImageIO.read(is), 200, 200);
            ImageIO.write(thumbnail, "jpg", baos);

            return CompletableFuture.completedFuture(baos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
