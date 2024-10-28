package com.fpt.locketcoupleapi.payload.response;

import com.fpt.locketcoupleapi.entity.User;
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
public class MessageResponse {
     int messageId;

     String messageContent;

     LocalDateTime sendDate;

     UserDTO userId;

     int photoId;
}
