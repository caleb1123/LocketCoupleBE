package com.fpt.locketcoupleapi.controller;

import com.fpt.locketcoupleapi.payload.DTO.MessageDTO;
import com.fpt.locketcoupleapi.payload.request.CreateMessageRequest;
import com.fpt.locketcoupleapi.payload.request.UpdateMessageRequest;
import com.fpt.locketcoupleapi.payload.response.ApiResponse;
import com.fpt.locketcoupleapi.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
@Slf4j
@CrossOrigin("http://localhost:3000")
public class MessageController {
    @Autowired
    MessageService messageService;

    @GetMapping("/all-message-photo/{photoId}")
    public ResponseEntity<ApiResponse<List<MessageDTO>>> getAllMessageByPhoto(@PathVariable int photoId) {
        List<MessageDTO> messageDTOS = messageService.getMessageByPhoto(photoId);
        ApiResponse<List<MessageDTO>> response = ApiResponse.<List<MessageDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("Successfully fetched Message")
                .data(messageDTOS)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<MessageDTO>>> getAllMessage() {
        List<MessageDTO> messageDTOS = messageService.getAllMessage();
        ApiResponse<List<MessageDTO>> response = ApiResponse.<List<MessageDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("Successfully fetched Message")
                .data(messageDTOS)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<MessageDTO>> createMessage(@RequestBody CreateMessageRequest messageRequest) {
        MessageDTO messageDTO = messageService.createMessage(messageRequest);
        ApiResponse<MessageDTO> response = ApiResponse.<MessageDTO>builder()
                .code(HttpStatus.CREATED.value())
                .message("Successfully created Message")
                .data(messageDTO)
                .build();
        return ResponseEntity.ok(response);
    }


}
