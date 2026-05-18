package com.financetracker.upload;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class FileStorageService {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".jpg", ".jpeg", ".png", ".pdf");

    private final Path uploadBaseDir;

    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.uploadBaseDir = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    public String store(MultipartFile file, UUID userId, UUID transactionId) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds 5MB limit");
        }

        String originalFilename = Path.of(
                file.getOriginalFilename() != null ? file.getOriginalFilename() : "file"
        ).getFileName().toString();
        String extension = getExtension(originalFilename).toLowerCase();

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Only JPG, PNG, and PDF files are allowed");
        }

        String filename = transactionId + "_" + originalFilename;
        Path userDir = uploadBaseDir.resolve(userId.toString());

        try {
            Files.createDirectories(userDir);
            Path targetPath = userDir.resolve(filename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            String relativePath = "uploads/" + userId + "/" + filename;
            log.info("Receipt stored at {}", relativePath);
            return relativePath;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public Path resolve(String userId, String filename) {
        return uploadBaseDir.resolve(userId).resolve(filename).normalize();
    }

    private String getExtension(String filename) {
        int dot = filename.lastIndexOf('.');
        return dot < 0 ? "" : filename.substring(dot);
    }
}
