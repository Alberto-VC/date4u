package com.tutego.date4u.core.photo;

import com.tutego.date4u.core.FileSystem;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Base64;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.assertj.core.api.BDDAssertions.as;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@SpringBootTest({ "spring.shell.interactive.enabled=false" })
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PhotoServiceTest {
    private static final byte[] MINIMAL_JPG = Base64.getDecoder().decode(
            "/9j/4AAQSkZJRgABAQEASABIAAD"
                    + "/2wBDAP////////////////////////////////////////////////////////////"
                    + "//////////////////////////wgALCAABAAEBAREA/8QAFBABAAAAAAAAAAAAAAAAA"
                    + "AAAAP/aAAgBAQABPxA=");                      // https://git.io/J9GXr

    @MockBean
    FileSystem fileSystem;

    @MockBean
    AwtBicubicThumbnail thumbnail;

    @Autowired
    PhotoService photoService;

    @BeforeEach
    void setupFileSystem() {
        given(fileSystem.getFreeDiskSpace()).willReturn(1000L);
        given(fileSystem.load(anyString())).willReturn(MINIMAL_JPG);
    }


    @Test
    public void successful_photo_upload() {
        String imageName = photoService.upload(MINIMAL_JPG);
        assertThat(imageName).isNotEmpty();
    }

    @Nested
    class Validator {
        @Test
        void photo_is_valid() {
            Photo photo = new Photo(1L, 1L, "fillmorespic", false, LocalDateTime.MIN);
            assertThatCode(() -> photoService.download(photo))
                    .doesNotThrowAnyException();
        }

        @Test
        void photo_has_invalid_created_date() {
            LocalDateTime future = LocalDateTime.of(2500, 1, 1, 0, 0, 0);
            Photo photo = new Photo(1L, 1L, "fillmorespic", false, future);
            assertThatThrownBy(() -> photoService.download(photo))
                    .isInstanceOf(ConstraintViolationException.class)
                    .extracting(
                            throwable ->
                                    ((ConstraintViolationException) throwable).getConstraintViolations(),
                            as(InstanceOfAssertFactories.collection(ConstraintViolation.class))
                    ).hasSize(1)
                    .first(InstanceOfAssertFactories.type(ConstraintViolation.class))
                    .satisfies(violation -> {
                        assertThat(violation.getRootBeanClass()).isSameAs(PhotoService.class);
                        assertThat(violation.getLeafBean()).isExactlyInstanceOf(Photo.class);
                        assertThat(violation.getPropertyPath()).hasToString("download.photo.created");
                        assertThat(violation.getInvalidValue()).isEqualTo(future);
                    });
        }
    }
}
