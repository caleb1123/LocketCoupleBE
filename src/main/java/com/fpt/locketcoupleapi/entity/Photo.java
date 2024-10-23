package com.fpt.locketcoupleapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Photo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int photoId;

    @Column
    private String photoUrl;

    @ManyToOne
    @JoinColumn(name = "couple_id")
    private Couple couple;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @Column
    private LocalDateTime createdDate;

    @Column
    private String photoName;

    @Column
    private boolean status;

    @OneToMany(mappedBy = "photo")
    private List<Message> messages;
}
