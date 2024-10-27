package com.fpt.locketcoupleapi.service.impl;

import com.fpt.locketcoupleapi.config.Util;
import com.fpt.locketcoupleapi.entity.Message;
import com.fpt.locketcoupleapi.entity.Photo;
import com.fpt.locketcoupleapi.payload.DTO.MessageDTO;
import com.fpt.locketcoupleapi.payload.request.CreateMessageRequest;
import com.fpt.locketcoupleapi.repository.MessageRepository;
import com.fpt.locketcoupleapi.repository.PhotoRepository;
import com.fpt.locketcoupleapi.service.MessageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    PhotoRepository photoRepository;

    @Autowired
    ModelMapper mapper;

    @Autowired
    Util util;


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
    public MessageDTO createMessage(CreateMessageRequest messageRequest) {
        Message message = new Message();
        message.setMessageContent(messageRequest.getMessageContent());
        message.setUser(util.getUserFromAuthentication());
        message.setSendDate(LocalDateTime.now());
        Photo photo = photoRepository.findById(messageRequest.getPhotoId())
                .orElseThrow(() -> new RuntimeException("Not found photo!"));
        message.setPhoto(photo);

        Message saved = messageRepository.save(message);

        MessageDTO messageDTO = mapper.map(saved, MessageDTO.class);
        messageDTO.setPhotoId(saved.getPhoto().getPhotoId());
        messageDTO.setUserId(saved.getUser().getUserId());

        return messageDTO;
    }

    @Override
    public void deleteMessage(int messageId) {

    }

    @Override
    public MessageDTO updateMessage(int messageId) {
        return null;
    }
}
