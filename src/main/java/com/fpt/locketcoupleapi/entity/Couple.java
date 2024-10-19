package com.fpt.locketcoupleapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Couple")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Couple {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int coupleId;

    @ManyToOne
    @JoinColumn(name = "user_boyfriend_id")
    private User userBoyfriend;

    @ManyToOne
    @JoinColumn(name = "user_girlfriend_id")
    private User userGirlfriend;

    @Column
    private String coupleName;

    @Column
    private String coupleAvatar;

    @Column
    private LocalDateTime createdDate;

    @Column
    private EStatus status;

    @OneToMany(mappedBy = "couple")
    private List<Photo> photos;
}
