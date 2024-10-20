package com.fpt.locketcoupleapi.payload.request;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class UserUpdateResquest {
    private String fullName;
    private String address;
    private String dob;
    private String email;
    private String sex;
}
