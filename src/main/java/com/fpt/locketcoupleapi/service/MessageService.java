package com.fpt.locketcoupleapi.service;

import com.fpt.locketcoupleapi.entity.Message;
import com.fpt.locketcoupleapi.payload.DTO.MessageDTO;

import java.util.List;

public interface MessageService {
    List<MessageDTO> getMessageByPhoto(int photoId);

    List<MessageDTO> getAllMessage();

    MessageDTO createMessage(MessageDTO messageDTO);

    void deleteMessage(int messageId);

    MessageDTO updateMessage(int messageId);

}
