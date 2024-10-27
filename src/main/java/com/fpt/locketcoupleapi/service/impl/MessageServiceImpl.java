package com.fpt.locketcoupleapi.service.impl;

import com.fpt.locketcoupleapi.entity.Message;
import com.fpt.locketcoupleapi.payload.DTO.MessageDTO;
import com.fpt.locketcoupleapi.repository.MessageRepository;
import com.fpt.locketcoupleapi.service.MessageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    ModelMapper mapper;


    @Override
    public List<MessageDTO> getMessageByPhoto(int photoId) {
        List<Message> messages = messageRepository.findByPhoto(photoId);
        if(messages.isEmpty()){
            return Collections.emptyList();
        }
        List<MessageDTO> messageDTOS = messages.stream()
                .map(message -> {
                    MessageDTO messageDTO = mapper.map(message, MessageDTO.class);

                    if (message.getPhoto() != null) {
                        messageDTO.setPhotoId(message.getPhoto().getPhotoId());
                    }
                    if (message.getUser() != null) {
                        messageDTO.setUserId(message.getUser().getUserId());
                    }

                    return messageDTO;
                })
                .collect(Collectors.toList());

        return messageDTOS;
    }

    @Override
    public List<MessageDTO> getAllMessage() {
        List<Message> messages = messageRepository.findAll();
        if(messages.isEmpty()){
            return Collections.emptyList();
        }
        List<MessageDTO> messageDTOS = messages.stream()
                .map(message -> {
                    MessageDTO messageDTO = mapper.map(message, MessageDTO.class);

                    if (message.getPhoto() != null) {
                        messageDTO.setPhotoId(message.getPhoto().getPhotoId());
                    }
                    if (message.getUser() != null) {
                        messageDTO.setUserId(message.getUser().getUserId());
                    }

                    return messageDTO;
                })
                .collect(Collectors.toList());

        return messageDTOS;
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
