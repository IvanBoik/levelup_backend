package com.boiko_ivan.spring.levelup_back.repositories;

import com.boiko_ivan.spring.levelup_back.entity.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {
    @Query(nativeQuery = true, value = """
    select files.* from files
    join lessons ON lessons.id_video = files.id
    where lessons.id = ?1
    """)
    Optional<FileInfo> findVideoByLessonId(long id);
}
