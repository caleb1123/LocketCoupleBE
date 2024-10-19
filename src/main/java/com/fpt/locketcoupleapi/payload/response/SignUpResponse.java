package com.fpt.locketcoupleapi.payload.response;

import lombok.*;

import java.util.Date;

@Data
@Builder
public class SignUpResponse {
    private int userId;
    private String fullName;
    private String userName;
    private String address;
    private String dob;
    private String email;
    private String sex;
    private String phone;
    private Date createdDate;
    private Date updatedDate;
    private boolean active;
}
