package com.boiko_ivan.spring.levelup_back.services;

import com.amazonaws.SdkClientException;
import com.boiko_ivan.spring.levelup_back.entity.FileInfo;
import com.boiko_ivan.spring.levelup_back.entity.CashedURL;
import com.boiko_ivan.spring.levelup_back.exceptions.EntityNotFoundException;
import com.boiko_ivan.spring.levelup_back.repositories.FileInfoRepository;
import com.boiko_ivan.spring.levelup_back.repositories.CashedURLRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileService {
    private final S3Service s3Service;
    private final CashedURLRepository cashedURLRepository;
    private final FileInfoRepository fileInfoRepository;

    @Transactional(rollbackOn = SdkClientException.class)
    public void updateFile(byte[] file, FileInfo fileInfo) {
        String key = fileInfo.getKey();
        s3Service.delete(key);
        fileInfo.setPermanentURL(null);
        String url = s3Service.uploadAndGetURL(file, key);
        CashedURL cashedURL = cashedURLRepository.findById(key)
                .orElse(new CashedURL(key));
        cashedURL.setUrl(url);
        cashedURL.setExpiration(createExpiration());
        cashedURLRepository.save(cashedURL);
        fileInfoRepository.save(fileInfo);
    }

    private Date createExpiration() {
        return new Date(System.currentTimeMillis() + S3Service.URL_EXPIRATION);
    }

    public FileInfo saveFile(byte[] file) {
        String key = s3Service.generateKey();
        String url = s3Service.uploadAndGetURL(file, key);
        FileInfo newVideoFileInfo = FileInfo.initWithKey(key);
        CashedURL cashedURL = CashedURL.builder()
                .key(key)
                .url(url)
                .expiration(createExpiration())
                .build();
        cashedURLRepository.save(cashedURL);
        return newVideoFileInfo;
    }

    public String getURL(FileInfo fileInfo) {
        if (fileInfo.getKey() != null) {
            Optional<CashedURL> optionalHashedURL = cashedURLRepository.findById(fileInfo.getKey());
            if (optionalHashedURL.isEmpty()) {
                return null;
            }
            CashedURL cashedURL = optionalHashedURL.get();
            if (cashedURL.getExpiration().before(new Date())) {
                String url = s3Service.getURL(fileInfo.getKey());
                cashedURL.setUrl(url);
                cashedURL.setExpiration(createExpiration());
                cashedURLRepository.save(cashedURL);
                return url;
            }
            else {
                return cashedURL.getUrl();
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

        cashedURLRepository.deleteById(fileInfo.getKey());
        s3Service.delete(fileInfo.getKey());
        fileInfoRepository.delete(fileInfo);
    }
}
