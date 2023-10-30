package com.boiko_ivan.spring.levelup_back.controllers;

import com.boiko_ivan.spring.levelup_back.entity.Message;
import com.boiko_ivan.spring.levelup_back.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    @MessageMapping("/chat")
    public void processMessage(@Payload Message message) {
        messageService.saveMessage(message);

        messagingTemplate.convertAndSendToUser(
                Long.toString(message.getFromID()),
                "/queue/messages",
                message
        );
    }
}
