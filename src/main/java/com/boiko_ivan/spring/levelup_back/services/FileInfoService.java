package com.boiko_ivan.spring.levelup_back.services;

import com.boiko_ivan.spring.levelup_back.entity.FileInfo;

import java.util.List;

public interface FileInfoService {
    List<FileInfo> getAllFiles();
    FileInfo getFileInfoByID(int id);
    void saveFileInfo(FileInfo fileInfo);
    void deleteFileInfo(int id);
    FileInfo getByKey(String key);
}
