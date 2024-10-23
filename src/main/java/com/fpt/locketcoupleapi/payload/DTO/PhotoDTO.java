package com.fpt.locketcoupleapi.payload.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhotoDTO {
    private int photoId;
    private String photoUrl;
    private LocalDateTime createdDate;
    private String photoName;
    private boolean status;
    private int coupleId;
}
