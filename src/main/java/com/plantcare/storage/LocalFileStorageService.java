package com.plantcare.storage;

import com.plantcare.common.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class LocalFileStorageService implements FileStorageService {

    private final Path fileStorageLocation;

    public LocalFileStorageService(@Value("${app.file-storage.upload-dir:./uploads}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create directory where uploaded files will be stored", ex);
        }
    }

    @Override
    public String upload(MultipartFile file, String folder) {
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (originalFileName.contains("..")) {
                throw new FileStorageException("Filename contains invalid path sequence " + originalFileName);
            }

            String fileExtension = "";
            int i = originalFileName.lastIndexOf('.');
            if (i > 0) fileExtension = originalFileName.substring(i);

            String newFileName = UUID.randomUUID().toString() + fileExtension;
            Path targetFolder = this.fileStorageLocation.resolve(folder);
            Files.createDirectories(targetFolder);

            Path targetLocation = targetFolder.resolve(newFileName);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
            }

            return folder + "/" + newFileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + originalFileName, ex);
        }
    }

    @Override
    public void delete(String storageKey) {
        try {
            Path filePath = this.fileStorageLocation.resolve(storageKey).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            // Log warning
        }
    }

    @Override
    public String getUrl(String storageKey) {
        return "/uploads/" + storageKey;
    }
}
