package com.fpt.locketcoupleapi.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fpt.locketcoupleapi.entity.Couple;
import com.fpt.locketcoupleapi.entity.EStatus;
import com.fpt.locketcoupleapi.entity.Photo;
import com.fpt.locketcoupleapi.entity.User;
import com.fpt.locketcoupleapi.exception.AppException;
import com.fpt.locketcoupleapi.exception.ErrorCode;
import com.fpt.locketcoupleapi.payload.DTO.PhotoDTO;
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
    public String uploadFileWithCouple(MultipartFile file) throws IOException {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUserName(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Couple couple = coupleRepository.findCoupleByUserBoyfriend_UserIdAndStatus(user.getUserId(), EStatus.ACCEPTED);
        if(couple == null) {
            couple = coupleRepository.findCoupleByUserGirlfriend_UserIdAndStatus(user.getUserId(), EStatus.ACCEPTED);
        }
        if(couple == null) {
            throw new AppException(ErrorCode.COUPLE_NOT_EXISTED);
        }

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        Photo photo = new Photo();
        photo.setPhotoUrl((String) uploadResult.get("url"));
        photo.setCouple(couple);
        photo.setCreatedDate(LocalDateTime.now());
        photo.setStatus(true);
        photoRepository.save(photo);
        return "Upload photo successfully";
    }

    @Override
    public List<PhotoDTO> findAll() {
        // Lấy tất cả Photo entities từ repository
        List<Photo> photos = photoRepository.findAll();

        // Chuyển đổi danh sách Photo entities sang PhotoDTOs
        return photos.stream()
                .map(photo -> modelMapper.map(photo, PhotoDTO.class)) // Mapping entity to DTO
                .collect(Collectors.toList());
    }
}
