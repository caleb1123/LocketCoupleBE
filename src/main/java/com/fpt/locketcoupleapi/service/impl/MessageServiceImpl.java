package com.fpt.locketcoupleapi.service.impl;

import com.fpt.locketcoupleapi.payload.DTO.MessageDTO;
import com.fpt.locketcoupleapi.repository.MessageRepository;
import com.fpt.locketcoupleapi.service.MessageService;
import com.nimbusds.oauth2.sdk.Message;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    ModelMapper mapper;


    @Override
    public MessageDTO getMessageByPhoto(int photoId) {
        return null;
    }

    @Override
    public MessageDTO getAllMessage() {
        return null;
    }

    @Override
    public MessageDTO createMessage(MessageDTO messageDTO) {
        return null;
    }

    @Override
    public void deleteMessage(int messageId) {

    }

    @Override
    public MessageDTO updateMessage(int messageId) {
        return null;
    }
}
