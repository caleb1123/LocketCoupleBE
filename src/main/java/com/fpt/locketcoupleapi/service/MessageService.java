package com.fpt.locketcoupleapi.service;

import com.fpt.locketcoupleapi.entity.Message;
import com.fpt.locketcoupleapi.payload.DTO.MessageDTO;
import com.fpt.locketcoupleapi.payload.request.CreateMessageRequest;

import java.util.List;

public interface MessageService {
    List<MessageDTO> getMessageByPhoto(int photoId);

    List<MessageDTO> getAllMessage();

    MessageDTO createMessage(CreateMessageRequest messageRequest);

    void deleteMessage(int messageId);

    MessageDTO updateMessage(int messageId);

}
