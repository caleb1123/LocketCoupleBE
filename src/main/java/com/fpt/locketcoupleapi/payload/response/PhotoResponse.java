package com.fpt.locketcoupleapi.payload.response;

import com.fpt.locketcoupleapi.entity.User;
import com.fpt.locketcoupleapi.payload.DTO.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhotoResponse {
    private int photoId;
    private String photoUrl;
    private LocalDateTime createdDate;
    private String photoName;
    private boolean status;
    private int coupleId;
    private UserDTO senderId;
}
