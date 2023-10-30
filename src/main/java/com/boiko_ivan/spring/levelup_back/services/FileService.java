package com.boiko_ivan.spring.levelup_back.services;

import com.amazonaws.SdkClientException;
import com.boiko_ivan.spring.levelup_back.entity.FileInfo;
import com.boiko_ivan.spring.levelup_back.entity.HashedURL;
import com.boiko_ivan.spring.levelup_back.exceptions.EntityNotFoundException;
import com.boiko_ivan.spring.levelup_back.repositories.FileInfoRepository;
import com.boiko_ivan.spring.levelup_back.repositories.HashedURLRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileService {
    private final S3Service s3Service;
    private final HashedURLRepository hashedURLRepository;
    private final FileInfoRepository fileInfoRepository;

    @Transactional(rollbackOn = RuntimeException.class)
    public void updateFile(byte[] file, FileInfo fileInfo) {
        try {
            String key = fileInfo.getKey();
            s3Service.delete(key);
            fileInfo.setPermanentURL(null);
            String url = s3Service.uploadAndGetURL(file, key);
            HashedURL hashedURL = hashedURLRepository.findById(key)
                    .orElse(new HashedURL(key));
            hashedURL.setUrl(url);
            hashedURL.setExpiration(new Date(System.currentTimeMillis() + S3Service.URL_EXPIRATION));
            hashedURLRepository.save(hashedURL);
            fileInfoRepository.save(fileInfo);
        }
        catch (SdkClientException e) {
            throw new RuntimeException("Error with S3", e);
        }
    }

    public FileInfo saveFile(byte[] file) {
        String key = s3Service.generateKey();
        String url = s3Service.uploadAndGetURL(file, key);
        FileInfo newVideoFileInfo = new FileInfo(key);
        HashedURL hashedURL = HashedURL.builder()
                .key(key)
                .url(url)
                .expiration(new Date(System.currentTimeMillis() + S3Service.URL_EXPIRATION))
                .build();
        hashedURLRepository.save(hashedURL);
        return newVideoFileInfo;
    }

    public String getURL(FileInfo fileInfo) {
        if (fileInfo != null && fileInfo.getKey() != null) {
            Optional<HashedURL> optionalHashedURL = hashedURLRepository.findById(fileInfo.getKey());
            if (optionalHashedURL.isEmpty()) {
                return null;
            }
            HashedURL hashedURL = optionalHashedURL.get();
            if (hashedURL.getExpiration().before(new Date())) {
                String url = s3Service.getURL(fileInfo.getKey());
                hashedURL.setUrl(url);
                hashedURL.setExpiration(new Date(System.currentTimeMillis() + S3Service.URL_EXPIRATION));
                hashedURLRepository.save(hashedURL);
                return url;
            }
            else {
                return hashedURL.getUrl();
            }
        }

        return null;
    }

    @Transactional(rollbackOn = {SdkClientException.class})
    public void deleteFile(long id) {
        FileInfo fileInfo = fileInfoRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("FileInfo with id = %d is not found".formatted(id))
                );

        hashedURLRepository.deleteById(fileInfo.getKey());
        s3Service.delete(fileInfo.getKey());
        fileInfoRepository.delete(fileInfo);
    }
}
