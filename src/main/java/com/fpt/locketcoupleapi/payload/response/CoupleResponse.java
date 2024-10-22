package com.fpt.locketcoupleapi.payload.response;

import com.fpt.locketcoupleapi.entity.ESex;
import com.fpt.locketcoupleapi.entity.EStatus;
import com.fpt.locketcoupleapi.payload.DTO.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoupleResponse {

    private int coupleId;
    private String coupleName;
    private String coupleAvatar;
    private LocalDateTime createdDate;
    private EStatus status;
    private ESex senderSex;
    private UserDTO lover;

}
