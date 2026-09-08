package com.plantcare.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String upload(MultipartFile file, String folder);
    void delete(String storageKey);
    String getUrl(String storageKey);
}
