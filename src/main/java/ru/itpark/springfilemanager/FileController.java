package ru.itpark.springfilemanager;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@RequestMapping("/upload")
@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@Slf4j
public class FileController {
    private final AmazonS3 client;

    @PostMapping
    public String uploadFile(@RequestParam MultipartFile file) {
        var key = UUID.randomUUID().toString();
        try {
            final File convertedFile = convertMultipartFileToFile(file);
            client.putObject("test", key, convertedFile);

            convertedFile.delete();
        } catch (Exception e) {
            log.info("error uploading file", e);
        } finally {
            log.info("key = " + key);
        }

        return "FileController.uploadFile | file = " + file.getOriginalFilename();
    }

    @SneakyThrows
    @PostMapping("/{key}")
    public byte[] getFile(@RequestBody String key) {
        final S3Object s3Object = client.getObject("test", key);

        final S3ObjectInputStream objectContent = s3Object.getObjectContent();

        return objectContent.readAllBytes();
    }

    public File convertMultipartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("error converting multipart file to file ", e);
        }
        return convertedFile;
    }

}
