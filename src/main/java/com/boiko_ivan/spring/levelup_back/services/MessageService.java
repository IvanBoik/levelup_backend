package com.boiko_ivan.spring.levelup_back.services;

import com.boiko_ivan.spring.levelup_back.entity.ChatRoom;
import com.boiko_ivan.spring.levelup_back.entity.Message;
import com.boiko_ivan.spring.levelup_back.exceptions.EntityNotFoundException;
import com.boiko_ivan.spring.levelup_back.repositories.ChatRoomRepository;
import com.boiko_ivan.spring.levelup_back.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;

    public Message findMessageByID(long id) {
        return messageRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Message with id = %d is not found".formatted(id))
                );
    }

    public ChatRoom findChatByID(long id) {
        return chatRoomRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Chat with id = %d is not found".formatted(id))
                );
    }

    public List<ChatRoom> findChatsByUserID(long id) {
        return chatRoomRepository.findByUserID(id);
    }

    public void saveMessage(Message message) {
        messageRepository.save(message);
    }

    public void saveChat(ChatRoom chat) {
        chatRoomRepository.save(chat);
    }
}
