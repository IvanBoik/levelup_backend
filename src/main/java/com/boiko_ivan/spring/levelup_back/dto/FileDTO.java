package com.boiko_ivan.spring.levelup_back.dto;

import com.boiko_ivan.spring.levelup_back.entity.FileInfo;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileDTO {
    private byte[] bytes;
    private FileInfo fileInfo;
}
