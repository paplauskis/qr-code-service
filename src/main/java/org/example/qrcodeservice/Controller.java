package org.example.qrcodeservice;

import org.example.qrcodeservice.exception.ContentsEmptyException;
import org.example.qrcodeservice.exception.WrongCorrectionLevelException;
import org.example.qrcodeservice.exception.WrongImageSizeException;
import org.example.qrcodeservice.exception.WrongImageTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.example.qrcodeservice.util.QrCodeConstants.*;

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

    @GetMapping("/qrcode")
    public ResponseEntity<?> respondQrCode(
            @RequestParam(value = "size", defaultValue = "250") int size,
            @RequestParam(value = "type", defaultValue = "png") String type,
            @RequestParam(value = "contents") String contents,
            @RequestParam(value = "correction", defaultValue = "L") String correctionLevel) {

        if (contents.isEmpty() || contents.isBlank()) {
            throw new ContentsEmptyException(ERROR_EMPTY_CONTENTS);
        }

        if (size < MIN_IMAGE_SIZE || size > MAX_IMAGE_SIZE) {
            throw new WrongImageSizeException(ERROR_WRONG_IMAGE_SIZE);
        }

        if (!CORRECTION_LEVELS.contains(correctionLevel)) {
            throw new WrongCorrectionLevelException(ERROR_WRONG_CORRECTION_LEVEL);
        }

        if (!SUPPORT_FORMATS.contains(type)) {
            throw new WrongImageTypeException(ERROR_WRONG_IMAGE_TYPE);
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

