package com.fpt.locketcoupleapi.payload.request;

import lombok.*;

@Data
@Builder
public class SignUpRequest {
    private String fullName;
    private String userName;
    private String password;
    private String email;
    private String sex;
}
