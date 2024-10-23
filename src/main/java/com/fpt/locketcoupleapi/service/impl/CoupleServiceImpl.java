package com.fpt.locketcoupleapi.service.impl;

import com.fpt.locketcoupleapi.entity.Couple;
import com.fpt.locketcoupleapi.entity.ESex;
import com.fpt.locketcoupleapi.entity.EStatus;
import com.fpt.locketcoupleapi.entity.User;
import com.fpt.locketcoupleapi.exception.AppException;
import com.fpt.locketcoupleapi.exception.ErrorCode;
import com.fpt.locketcoupleapi.payload.DTO.CoupleDTO;
import com.fpt.locketcoupleapi.payload.DTO.UserDTO;
import com.fpt.locketcoupleapi.payload.response.CoupleResponse;
import com.fpt.locketcoupleapi.payload.response.SendRequestResponse;
import com.fpt.locketcoupleapi.repository.CoupleRepository;
import com.fpt.locketcoupleapi.repository.UserRepository;
import com.fpt.locketcoupleapi.service.CoupleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
        if(sender.getUserId() == userId) {
            throw new AppException(ErrorCode.MY_INFOR);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        long count;
        if(sender.getSex() == ESex.MALE){

            count = coupleRepository.countCouplesByUserBoyfriend_UserIdAndStatus(sender.getUserId(), EStatus.PENDING);

        }else if(sender.getSex() == ESex.FEMALE){
            count = coupleRepository.countCouplesByUserGirlfriend_UserIdAndStatus(sender.getUserId(), EStatus.PENDING);
        }
        else {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        if(count >=1 ) {
            throw new AppException(ErrorCode.COUPLE_EXISTED);
        }

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
        couple.setSenderSex(sender.getSex());
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

    @Override
    public List<CoupleDTO> getAllCouple() {
        List<Couple> couples = coupleRepository.findAll();
        if(couples.isEmpty()) {
            throw new AppException(ErrorCode.COUPLE_NOT_FOUND);
        }
        return couples.stream()
                .map(couple -> modelMapper.map(couple, CoupleDTO.class))  // Map each Couple to CoupleDTO
                .collect(Collectors.toList());
    }

    @Override
    public SendRequestResponse getMyCoupleByPending() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUserName(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Couple couples = new Couple();

            couples = coupleRepository.findCoupleByUserBoyfriend_UserIdAndStatus(user.getUserId(), EStatus.PENDING);
            if(couples == null) {
                couples = coupleRepository.findCoupleByUserGirlfriend_UserIdAndStatus(user.getUserId(), EStatus.PENDING);
            }


        // Check if couples list is empty
        if (couples == null) {
            throw new AppException(ErrorCode.COUPLE_NOT_FOUND);
        }
        User response = new User();
        if(couples.getSenderSex() == ESex.FEMALE){
            response = couples.getUserBoyfriend();
        }else {
            response = couples.getUserGirlfriend();
        }
        if(couples.getSenderSex() == user.getSex()) {
            throw new AppException(ErrorCode.COUPLE_NOT_FOUND);
        }
        SendRequestResponse userDTOs = modelMapper.map(response,SendRequestResponse.class);
        userDTOs.setUserId(response.getUserId());
        userDTOs.setCoupleId(couples.getCoupleId());
        // Map each Couple to CoupleDTO
        return userDTOs;
    }

    @Override
    public void CancelRequest(int requestId) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUserName(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Couple couple = coupleRepository.findById(requestId)
                .orElseThrow(() -> new AppException(ErrorCode.COUPLE_NOT_FOUND));
        if(couple.getUserGirlfriend().getUserId() == user.getUserId()) {
            couple.setStatus(EStatus.DECLINED);
        }else if(couple.getUserBoyfriend().getUserId() == user.getUserId()) {
            couple.setStatus(EStatus.DECLINED);
        }else {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        coupleRepository.save(couple);

    }

    @Override
    public SendRequestResponse getSendRequest() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUserName(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Couple couples = new Couple();
        if (user.getSex() == ESex.MALE || user.getSex() == ESex.FEMALE) {
            couples = coupleRepository.findCoupleByUserBoyfriend_UserIdAndStatus(user.getUserId(), EStatus.PENDING);
            if(couples == null) {
                couples = coupleRepository.findCoupleByUserGirlfriend_UserIdAndStatus(user.getUserId(), EStatus.PENDING);
            }
        }

        // Check if couples list is empty
        if (couples == null) {
            throw new AppException(ErrorCode.COUPLE_NOT_FOUND);
        }
        User response = new User();
        if(couples.getSenderSex() == ESex.FEMALE){
            response = couples.getUserBoyfriend();
        }else {
            response = couples.getUserGirlfriend();
        }
        if(couples.getSenderSex() != user.getSex()) {
            throw new AppException(ErrorCode.COUPLE_NOT_FOUND);
        }
        SendRequestResponse userDTOs = modelMapper.map(response,SendRequestResponse.class);
        userDTOs.setUserId(user.getUserId());
        userDTOs.setCoupleId(couples.getCoupleId());
        // Map each Couple to CoupleDTO
        return userDTOs;
    }

    @Override
    public CoupleResponse getMyCoupleByComplet() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUserName(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Couple couple = coupleRepository.findCoupleByUserBoyfriend_UserIdAndStatus(user.getUserId(),EStatus.ACCEPTED);
        if (couple == null) {
            couple = coupleRepository.findCoupleByUserGirlfriend_UserIdAndStatus(user.getUserId(),EStatus.ACCEPTED);
        }
        CoupleResponse coupleResponse = new CoupleResponse();
        coupleResponse = modelMapper.map(couple,CoupleResponse.class);
        if(couple.getUserBoyfriend().getUserId() == user.getUserId()){
            coupleResponse.setLover(modelMapper.map(couple.getUserGirlfriend(),UserDTO.class));
        }else if(couple.getUserGirlfriend().getUserId() == user.getUserId()){
            coupleResponse.setLover(modelMapper.map((couple.getUserBoyfriend()),UserDTO.class));
        }

        return coupleResponse;
    }


}
