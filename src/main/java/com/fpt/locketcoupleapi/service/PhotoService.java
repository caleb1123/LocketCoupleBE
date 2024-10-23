package com.fpt.locketcoupleapi.service;

import com.fpt.locketcoupleapi.payload.DTO.PhotoDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PhotoService {
    String uploadFileWithCouple(MultipartFile file) throws IOException;

    List<PhotoDTO> findAll();
}
