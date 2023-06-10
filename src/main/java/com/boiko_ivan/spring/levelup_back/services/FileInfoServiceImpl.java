package com.boiko_ivan.spring.levelup_back.services;

import com.boiko_ivan.spring.levelup_back.entity.FileInfo;
import com.boiko_ivan.spring.levelup_back.repositories.FileInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileInfoServiceImpl implements FileInfoService {
    @Autowired
    private FileInfoRepository fileInfoRepository;

    @Override
    public List<FileInfo> getAllFiles() {
        return fileInfoRepository.findAll();
    }

    @Override
    public FileInfo getFileInfoByID(int id) {
        return fileInfoRepository.findById(id).orElse(null);
    }

    @Override
    public void saveFileInfo(FileInfo fileInfo) {
        fileInfoRepository.save(fileInfo);
    }

    @Override
    public void deleteFileInfo(int id) {
        fileInfoRepository.deleteById(id);
    }

    @Override
    public FileInfo getByKey(String key) {
        return fileInfoRepository.getByKeyEquals(key);
    }
}
