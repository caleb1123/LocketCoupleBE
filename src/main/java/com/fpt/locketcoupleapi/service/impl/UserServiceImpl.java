package com.fpt.locketcoupleapi.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fpt.locketcoupleapi.entity.ESex;
import com.fpt.locketcoupleapi.entity.User;
import com.fpt.locketcoupleapi.exception.AppException;
import com.fpt.locketcoupleapi.exception.ErrorCode;
import com.fpt.locketcoupleapi.payload.DTO.UserDTO;
import com.fpt.locketcoupleapi.payload.request.UserUpdateResquest;
import com.fpt.locketcoupleapi.payload.response.FindUserResponse;
import com.fpt.locketcoupleapi.payload.response.MyInforUserResponse;
import com.fpt.locketcoupleapi.repository.UserRepository;
import com.fpt.locketcoupleapi.service.UserService;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MailService mailService;
    @Autowired
    private Cloudinary cloudinary;

    private ConcurrentHashMap<String, OTPDetails> otpStore = new ConcurrentHashMap<>();

    @Override
    public FindUserResponse findUserByUsername(String username) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User myinfor = userRepository.findByUserName(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if(myinfor.getUserName().equals(username)){
            throw new AppException(ErrorCode.MY_INFOR);
        }
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        FindUserResponse response = new FindUserResponse();
        response.setFullName(user.getFullName());
        response.setUserName(user.getUserName());
        response.setAddress(user.getAddress());
        response.setEmail(user.getEmail());
        response.setAvatarUrl(user.getAvatarUrl());
        return response;



    }

    @Override
    public MyInforUserResponse getMyInfor() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUserName(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        MyInforUserResponse response = new MyInforUserResponse();
        response.setFullName(user.getFullName());
        response.setUserName(user.getUserName());
        response.setAddress(user.getAddress());
        response.setEmail(user.getEmail());
        response.setAvatarUrl(user.getAvatarUrl());
        response.setDob(user.getDob());
        response.setSex(String.valueOf(user.getSex()));
        return response;
    }

    @Override
    public UserDTO updateMyInfor(UserUpdateResquest userDTO) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        // Retrieve the user from the repository
        User user = userRepository.findByUserName(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Update fields only if they are not null
        if (userDTO.getFullName() != null) {
            user.setFullName(userDTO.getFullName());
        }
        if (userDTO.getAddress() != null) {
            user.setAddress(userDTO.getAddress());
        }
        if (userDTO.getSex() != null) {
            user.setSex(ESex.valueOf(userDTO.getSex()));
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getDob() != null) {
            user.setDob(userDTO.getDob());
        }

        // Save the updated user back to the repository
        userRepository.save(user);
        UserDTO userDTO1 = new UserDTO();
        userDTO1.setUserId(user.getUserId());
        userDTO1.setFullName(user.getFullName());
        userDTO1.setUserName(user.getUserName());
        userDTO1.setAddress(user.getAddress());
        userDTO1.setEmail(user.getEmail());
        userDTO1.setDob(user.getDob());
        userDTO1.setSex(String.valueOf(user.getSex()));
        userDTO1.setActive(user.isActive());
        return userDTO1;
    }

    @Override
    public String removeUser() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUserName(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setActive(false);
        userRepository.save(user);
        return "Delete success";
    }

    @Override
    public void createandsendOTP(String email) throws MessagingException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        String otp = generateOTP(); // Tạo mã OTP ngẫu nhiên
        long expirationTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5); // Thiết lập thời gian hết hạn (5 phút)

        // Lưu mã OTP và thời gian hết hạn vào otpStore
        otpStore.put(user.getEmail(), new OTPDetails(otp, expirationTime));

        // Gửi OTP qua email
        mailService.sendOTPtoActiveAccount(user.getEmail(), otp,user.getFullName());
    }

    @Override
    public boolean verifyOTP(String email,String otp) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        OTPDetails otpDetails = otpStore.get(user.getEmail());
        if (otpDetails == null) {
            return false;
        }
        if (otpDetails.expirationTime < System.currentTimeMillis()) {
            return false;
        }
        if (!otpDetails.otp.equals(otp)) {
            return false;
        }
        user.setActive(true);
        userRepository.save(user);
        return true;
    }

    @Override
    public String uploadImageToCloudinary(MultipartFile file) throws IOException {
        var name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserName(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        user.setAvatarUrl((String) uploadResult.get("url"));
        userRepository.save(user);
        return (String) uploadResult.get("url");
    }

    // Phương thức tạo mã OTP ngẫu nhiên (6 chữ số)
    private String generateOTP() {
        return String.format("%06d", (int) (Math.random() * 1000000));
    }



    // Lớp lưu trữ chi tiết mã OTP
    private static class OTPDetails {
        String otp;
        long expirationTime;

        OTPDetails(String otp, long expirationTime) {
            this.otp = otp;
            this.expirationTime = expirationTime;
        }
    }


}
