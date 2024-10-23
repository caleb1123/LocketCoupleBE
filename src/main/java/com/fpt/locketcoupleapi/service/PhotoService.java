package com.fpt.locketcoupleapi.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PhotoService {
    String uploadFileWithCouple(MultipartFile file) throws IOException;
}
