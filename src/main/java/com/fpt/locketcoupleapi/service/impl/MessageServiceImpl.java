package com.fpt.locketcoupleapi.service.impl;

import com.fpt.locketcoupleapi.config.Util;
import com.fpt.locketcoupleapi.entity.Message;
import com.fpt.locketcoupleapi.entity.Photo;
import com.fpt.locketcoupleapi.entity.User;
import com.fpt.locketcoupleapi.exception.AppException;
import com.fpt.locketcoupleapi.exception.ErrorCode;
import com.fpt.locketcoupleapi.payload.DTO.MessageDTO;
import com.fpt.locketcoupleapi.payload.DTO.UserDTO;
import com.fpt.locketcoupleapi.payload.request.CreateMessageRequest;
import com.fpt.locketcoupleapi.payload.request.UpdateMessageRequest;
import com.fpt.locketcoupleapi.repository.MessageRepository;
import com.fpt.locketcoupleapi.repository.PhotoRepository;
import com.fpt.locketcoupleapi.repository.UserRepository;
import com.fpt.locketcoupleapi.service.MessageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private UserRepository userRepository;

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
                        messageDTO.setUserId(mapper.map(message.getUser(), UserDTO.class));
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
                        messageDTO.setUserId(mapper.map(message.getUser(), UserDTO.class));
                    }

                    return messageDTO;
                })
                .collect(Collectors.toList());

        return messageDTOS;
    }

    @Override
    public MessageDTO createMessage(CreateMessageRequest messageRequest) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUserName(name)
                .orElseThrow(() -> new RuntimeException("Not found User!"));

        Message message = new Message();
        message.setMessageContent(messageRequest.getMessageContent());
        message.setUser(user);
        message.setSendDate(LocalDateTime.now());
        Photo photo = photoRepository.findById(messageRequest.getPhotoId())
                .orElseThrow(() -> new AppException(ErrorCode.PHOTO_NOT_EXISTED));
        message.setPhoto(photo);


        Message saved = messageRepository.save(message);

        MessageDTO messageDTO = mapper.map(saved, MessageDTO.class);
        messageDTO.setPhotoId(saved.getPhoto().getPhotoId());
        messageDTO.setUserId(mapper.map(saved.getUser(), UserDTO.class));

        return messageDTO;
    }

    @Override
    public void deleteMessage(int messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Not found Message!"));

        messageRepository.delete(message);
    }

    @Override
    public MessageDTO updateMessage(UpdateMessageRequest messageRequest) {
        Message message = messageRepository.findById(messageRequest.getMessageId())
                .orElseThrow(() -> new RuntimeException("Not found Message!"));
        message.setMessageContent(messageRequest.getMessageContent());
        message.setSendDate(LocalDateTime.now());

        Message saved = messageRepository.save(message);

        MessageDTO messageDTO = mapper.map(saved, MessageDTO.class);
        messageDTO.setPhotoId(saved.getPhoto().getPhotoId());
        messageDTO.setUserId(mapper.map(saved.getUser(), UserDTO.class));

        return messageDTO;
    }
}
