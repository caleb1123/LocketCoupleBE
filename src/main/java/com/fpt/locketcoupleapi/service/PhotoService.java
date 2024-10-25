package com.fpt.locketcoupleapi.service;

import com.fpt.locketcoupleapi.payload.DTO.PhotoDTO;
import com.fpt.locketcoupleapi.payload.response.PhotoResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PhotoService {
    String uploadFileWithCouple(MultipartFile file,String title) throws IOException;

    List<PhotoResponse> findAll();

    List<PhotoResponse> findByCoupleId();

    List<PhotoResponse> findByLover();
}
