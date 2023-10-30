package com.boiko_ivan.spring.levelup_back.repositories;

import com.boiko_ivan.spring.levelup_back.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query(nativeQuery = true, value = """
    select * from chat_rooms
    where id_student = ?1 or id_techer = ?1
    """)
    List<ChatRoom> findByUserID(long id);
}
