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

    @Override
    public void acceptRequest(int requestId) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUserName(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Couple couple = coupleRepository.findById(requestId)
                .orElseThrow(() -> new AppException(ErrorCode.COUPLE_NOT_FOUND));

        if(couple.getUserGirlfriend().getUserId() == user.getUserId()) {
            couple.setStatus(EStatus.ACCEPTED);
            coupleRepository.save(couple);
        }else if(couple.getUserBoyfriend().getUserId() == user.getUserId()) {
            couple.setStatus(EStatus.ACCEPTED);
            coupleRepository.save(couple);
        }else {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }


    }
}
