package com.fpt.locketcoupleapi.payload.DTO;

import com.fpt.locketcoupleapi.entity.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoupleDTO {

    private int coupleId;
    private String coupleName;
    private String coupleAvatar;
    private LocalDateTime createdDate;
    private EStatus status;
    private int userBoyfriendId;
    private int userGirlfriendId;
}
