package com.boiko_ivan.spring.levelup_back.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class PasswordMailSender {
    @Autowired
    private JavaMailSender mailSender;

    public void sendGeneratedPassword(String to, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Пароль для LevelUp");
        message.setText("Поскольку вы создали аккаунт LevelUp без использование пароля, мы сгенерировали его для вас. " +
                "Вы можете входить в свой аккаунт с его помощью, а так же всегда можете поменять его в личном кабинете.\n" +
                "Ваш пароль: " + password);
        mailSender.send(message);
    }
}
