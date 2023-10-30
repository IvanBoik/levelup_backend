package com.boiko_ivan.spring.levelup_back.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "id_from")
    private long fromID;

    @Column(name = "id_to")
    private long toID;

    @Column(name = "id_chat")
    private long chatID;

    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "message_attachments", joinColumns = @JoinColumn(name = "id_message"),
            inverseJoinColumns = @JoinColumn(name = "id"))
    private List<FileInfo> attachments = new ArrayList<>();

    private String content;

    private Timestamp timestamp;
}
