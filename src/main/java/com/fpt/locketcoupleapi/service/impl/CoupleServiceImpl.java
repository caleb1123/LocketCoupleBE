package com.fpt.locketcoupleapi.service.impl;

import com.fpt.locketcoupleapi.entity.Couple;
import com.fpt.locketcoupleapi.entity.ESex;
import com.fpt.locketcoupleapi.entity.EStatus;
import com.fpt.locketcoupleapi.entity.User;
import com.fpt.locketcoupleapi.exception.AppException;
import com.fpt.locketcoupleapi.exception.ErrorCode;
import com.fpt.locketcoupleapi.repository.CoupleRepository;
import com.fpt.locketcoupleapi.repository.UserRepository;
import com.fpt.locketcoupleapi.service.CoupleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class CoupleServiceImpl implements CoupleService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    CoupleRepository coupleRepository;
    @Autowired
    UserRepository userRepository;
    @Override
    public void sendRequest(int userId) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User sender = userRepository.findByUserName(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Couple couple = new Couple();
        if(sender.getSex() == ESex.MALE) {
            couple.setUserBoyfriend(sender);
            couple.setUserGirlfriend(user);
        } else {
            couple.setUserBoyfriend(user);
            couple.setUserGirlfriend(sender);
        }
        couple.setStatus(EStatus.PENDING);
        couple.setCreatedDate(LocalDateTime.now());
        coupleRepository.save(couple);


    }
}
