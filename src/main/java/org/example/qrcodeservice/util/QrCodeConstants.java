package org.example.qrcodeservice.util;

import org.springframework.http.MediaType;

import java.util.Map;
import java.util.Set;

public class QrCodeConstants {

    public static final int MIN_IMAGE_SIZE = 150;

    public static final int MAX_IMAGE_SIZE = 350;

    public static final Set<String> SUPPORT_FORMATS = Set.of("png", "jpeg", "gif");

    public static final Set<String> CORRECTION_LEVELS = Set.of("L", "M", "Q", "H");

    public static final Map<String, MediaType> MEDIA_TYPES = Map.of(
            "png", MediaType.IMAGE_PNG,
            "jpeg", MediaType.IMAGE_JPEG,
            "gif", MediaType.IMAGE_GIF
    );

    public static final String ERROR_EMPTY_CONTENTS = "Contents cannot be null or blank";
    public static final String ERROR_WRONG_IMAGE_SIZE = "Image size must be between 150 and 350 pixels";
    public static final String ERROR_WRONG_CORRECTION_LEVEL = "Permitted error correction levels are L, M, Q, H";
    public static final String ERROR_WRONG_IMAGE_TYPE = "Only png, jpeg, and gif image types are supported";
}
