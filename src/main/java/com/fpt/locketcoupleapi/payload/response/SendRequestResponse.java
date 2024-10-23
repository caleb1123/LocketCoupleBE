package com.fpt.locketcoupleapi.payload.response;

import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendRequestResponse {
    private int userId;
    private String fullName;
    private String userName;
    private String avatarUrl;
    private String address;
    private String dob;
    private String email;
    private String sex;
    private Date createdDate;
    private boolean active;
    private int coupleId;
}
