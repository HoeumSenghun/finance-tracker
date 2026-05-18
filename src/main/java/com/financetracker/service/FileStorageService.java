package com.financetracker.service;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.UUID;

public interface FileStorageService {

    String store(MultipartFile file, UUID userId, UUID transactionId);

    Path resolve(String userId, String filename);
}
