package com.fpt.locketcoupleapi.service;

import com.fpt.locketcoupleapi.payload.DTO.UserDTO;
import com.fpt.locketcoupleapi.payload.request.UserUpdateResquest;
import com.fpt.locketcoupleapi.payload.response.FindUserResponse;
import com.fpt.locketcoupleapi.payload.response.MyInforUserResponse;
import jakarta.mail.MessagingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    FindUserResponse findUserByUsername(String username);

    MyInforUserResponse getMyInfor();

    UserDTO updateMyInfor(UserUpdateResquest userDTO);

    String removeUser();

    void createandsendOTP(String email) throws MessagingException;

    boolean verifyOTP(String email,String otp);

    String uploadImageToCloudinary(MultipartFile file) throws IOException;
}
