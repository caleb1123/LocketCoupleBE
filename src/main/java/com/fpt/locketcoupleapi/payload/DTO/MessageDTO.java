package com.fpt.locketcoupleapi.payload.DTO;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
     int messageId;

     String messageContent;

     LocalDateTime sendDate;

     int userId;

     int photoId;
}
