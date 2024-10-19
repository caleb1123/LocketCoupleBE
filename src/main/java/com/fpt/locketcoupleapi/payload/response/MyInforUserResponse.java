package com.fpt.locketcoupleapi.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyInforUserResponse {
    private String fullName;
    private String userName;
    private String address;
    private String dob;
    private String email;
    private Boolean sex;
    private String phone;
    private String avatarUrl;
}
