package org.example.qrcodeservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class Controller {

    final QrCodeService qrCodeService;

    public Controller(QrCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @GetMapping("/health")
    public ResponseEntity<String> respondHealth() {
        return new ResponseEntity<>("200 OK", HttpStatus.OK);
    }

    static final Set<String> SUPPORT_FORMATS = Set.of("png", "jpeg", "gif");
    static final Map<String, MediaType> MEDIA_TYPES = Map.of(
            "png", MediaType.IMAGE_PNG,
            "jpeg", MediaType.IMAGE_JPEG,
            "gif", MediaType.IMAGE_GIF
    );

    @GetMapping("/qrcode")
    public ResponseEntity<?> respondQrCode(
            @RequestParam(value = "size", defaultValue = "250") int size,
            @RequestParam(value = "type", defaultValue = "png") String type,
            @RequestParam(value = "contents") String contents,
            @RequestParam(value = "correction", defaultValue = "L") String correctionLevel) {

        if (contents.isEmpty() || contents.isBlank()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Contents cannot be null or blank"));
        }

        if (size < 150 || size > 350) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Image size must be between 150 and 350 pixels"));
        }

        if (!correctionLevel.equals("L") && !correctionLevel.equals("M") &&
                !correctionLevel.equals("Q") && !correctionLevel.equals("H")) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Permitted error correction levels are L, M, Q, H"));
        }

        if (!SUPPORT_FORMATS.contains(type)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Only png, jpeg and gif image types are supported"));
        }

        BufferedImage qrCode = qrCodeService.getQrCode(contents, size, correctionLevel);

        try (var baos = new ByteArrayOutputStream()) {
            ImageIO.write(qrCode, type, baos);
            byte[] bytes = baos.toByteArray();
            return ResponseEntity
                    .ok()
                    .contentType(MEDIA_TYPES.get(type))
                    .body(bytes);
        } catch (IOException e) {
            System.out.println("ERROR" + e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}

