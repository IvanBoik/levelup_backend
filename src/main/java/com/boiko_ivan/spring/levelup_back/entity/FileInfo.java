package com.boiko_ivan.spring.levelup_back.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "files")
public class FileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "file_key")
    private String key;

    @Column(name = "permanent_url")
    private String permanentURL;

    public static FileInfo initWithKey(String key) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setKey(key);
        return fileInfo;
    }

    public static FileInfo initWithPermanentURL(String url) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setPermanentURL(url);
        return fileInfo;
    }
}
