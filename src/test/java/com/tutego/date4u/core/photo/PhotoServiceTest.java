package com.tutego.date4u.core.photo;

import com.tutego.date4u.core.FileSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Base64;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PhotoServiceTest {
    private static final byte[] MINIMAL_JPG = Base64.getDecoder().decode(
            "/9j/4AAQSkZJRgABAQEASABIAAD"
                    + "/2wBDAP////////////////////////////////////////////////////////////"
                    + "//////////////////////////wgALCAABAAEBAREA/8QAFBABAAAAAAAAAAAAAAAAA"
                    + "AAAAP/aAAgBAQABPxA=");                      // https://git.io/J9GXr

    @Mock
    FileSystem fileSystem;
    @Spy
    AwtBicubicThumbnail thumbnail;
    @InjectMocks
    PhotoService photoService;

    @BeforeEach
    void setupFileSystem() {
        given(fileSystem.getFreeDiskSpace()).willReturn(1000L);
        given(fileSystem.load(anyString())).willReturn(MINIMAL_JPG);
    }

    @Test
    public void successful_photo_upload() {
        String imageName = photoService.upload(MINIMAL_JPG);
        assertThat(imageName).isNotNull();
        verify(fileSystem, times(2)).store(anyString(), any(byte[].class));
        verify(thumbnail).thumbnail(any(byte[].class));
    }
}
