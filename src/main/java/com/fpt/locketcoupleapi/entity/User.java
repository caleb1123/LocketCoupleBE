package com.fpt.locketcoupleapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Account")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column
    private String fullName;

    @Column(unique = true)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column
    private String address;

    @Column
    private String dob;

    @Column(unique = true)
    private String email;

    @Column
    private ESex sex;

    @Column(unique = true)
    private String phone;

    @Column
    private Date createdDate;

    @Column
    private Date updatedDate;

    @Column
    private boolean active;

    @OneToMany(mappedBy = "userBoyfriend")
    private List<Couple> coupleBoyfriend;

    @OneToMany(mappedBy = "userGirlfriend")
    private List<Couple> coupleGirlfriend;


}
