package com.fpt.locketcoupleapi.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindUserResponse {
    private int userId;
    private String fullName;
    private String userName;
    private String address;
    private String dob;
    private String email;
    private String avatarUrl;
    private Boolean sex;
}
