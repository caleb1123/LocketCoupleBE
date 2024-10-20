package com.fpt.locketcoupleapi.payload.DTO;

import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private int userId;
    private String fullName;
    private String userName;
    private String address;
    private String dob;
    private String email;
    private String sex;
    private Date createdDate;
    private boolean active;
}
