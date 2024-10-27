package com.fpt.locketcoupleapi.service;

import com.fpt.locketcoupleapi.entity.Message;
import com.fpt.locketcoupleapi.payload.DTO.MessageDTO;

public interface MessageService {
    MessageDTO getMessageByPhoto(int photoId);

    MessageDTO getAllMessage();

    MessageDTO createMessage(MessageDTO messageDTO);

    void deleteMessage(int messageId);

    MessageDTO updateMessage(int messageId);

}
