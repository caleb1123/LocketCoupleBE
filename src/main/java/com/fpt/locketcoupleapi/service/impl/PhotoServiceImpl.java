package com.fpt.locketcoupleapi.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fpt.locketcoupleapi.entity.*;
import com.fpt.locketcoupleapi.exception.AppException;
import com.fpt.locketcoupleapi.exception.ErrorCode;
import com.fpt.locketcoupleapi.payload.DTO.PhotoDTO;
import com.fpt.locketcoupleapi.payload.response.PhotoResponse;
import com.fpt.locketcoupleapi.repository.CoupleRepository;
import com.fpt.locketcoupleapi.repository.PhotoRepository;
import com.fpt.locketcoupleapi.repository.UserRepository;
import com.fpt.locketcoupleapi.service.PhotoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PhotoServiceImpl implements PhotoService {
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private CoupleRepository coupleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private Cloudinary cloudinary;

    @Override
    public String uploadFileWithCouple(MultipartFile file, String title) throws IOException {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUserName(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Couple couple = coupleRepository.findCoupleByUserBoyfriend_UserIdAndStatus(user.getUserId(), EStatus.ACCEPTED);
        if (couple == null) {
            couple = coupleRepository.findCoupleByUserGirlfriend_UserIdAndStatus(user.getUserId(), EStatus.ACCEPTED);
        }
        if (couple == null) {
            throw new AppException(ErrorCode.COUPLE_NOT_EXISTED);
        }

        // Kiểm tra xem tệp là hình ảnh hay video
        Map uploadResult;
        if (file.getContentType() != null && file.getContentType().startsWith("video/")) {
            // Nếu là video, chỉ định loại tệp là video
            uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "video"));
        } else {
            // Nếu không phải video, xử lý như ảnh thông thường
            uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        }

        // Tạo đối tượng Photo và lưu thông tin
        Photo photo = new Photo();
        photo.setPhotoUrl((String) uploadResult.get("url")); // URL của ảnh hoặc video sau khi upload
        photo.setCouple(couple);
        photo.setCreatedDate(LocalDateTime.now());
        photo.setPhotoName(title);
        photo.setStatus(true);
        photo.setSender(user);
        photoRepository.save(photo);

        return "Upload file successfully";
    }


    @Override
    public List<PhotoResponse> findAll() {
        // Lấy tất cả Photo entities từ repository
        List<Photo> photos = photoRepository.findAll();
        if (photos.isEmpty()) {
            throw new AppException(ErrorCode.PHOTO_NOT_EXISTED);
        }
        // Chuyển đổi danh sách Photo entities sang PhotoDTOs
        return photos.stream()
                .map(photo -> modelMapper.map(photo, PhotoResponse.class)) // Mapping entity to DTO
                .collect(Collectors.toList());
    }

    @Override
    public List<PhotoResponse> findByCoupleId() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUserName(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Couple couple = coupleRepository.findCoupleByUserBoyfriend_UserIdAndStatus(user.getUserId(), EStatus.ACCEPTED);
        if (couple == null) {
            couple = coupleRepository.findCoupleByUserGirlfriend_UserIdAndStatus(user.getUserId(), EStatus.ACCEPTED);
        }
        if (couple == null) {
            throw new AppException(ErrorCode.COUPLE_NOT_EXISTED);
        }

        List<Photo> photos = photoRepository.findPhotosByCouple_CoupleId(couple.getCoupleId());
        if (photos.isEmpty()) {
            throw new AppException(ErrorCode.PHOTO_NOT_EXISTED);
        }
        return photos.stream()
                .map(photo -> modelMapper.map(photo, PhotoResponse.class)) // Mapping entity to DTO
                .collect(Collectors.toList());
    }

    @Override
    public List<PhotoResponse> findByLover() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUserName(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Couple couple = coupleRepository.findCoupleByUserBoyfriend_UserIdAndStatus(user.getUserId(), EStatus.ACCEPTED);
        if (couple == null) {
            couple = coupleRepository.findCoupleByUserGirlfriend_UserIdAndStatus(user.getUserId(), EStatus.ACCEPTED);
        }
        if (couple == null) {
            throw new AppException(ErrorCode.COUPLE_NOT_EXISTED);
        }

        List<Photo> photos = photoRepository.findPhotosBySender_UserId(
                user.getSex() == ESex.MALE
                        ? couple.getUserGirlfriend().getUserId()
                        : couple.getUserBoyfriend().getUserId()
        );

        if (photos.isEmpty()) {
            throw new AppException(ErrorCode.PHOTO_NOT_EXISTED);
        }

        return photos.stream()
                .map(photo -> modelMapper.map(photo, PhotoResponse.class)) // Mapping entity to DTO
                .collect(Collectors.toList());
    }
}
