package com.fpt.locketcoupleapi.service.impl;

import com.fpt.locketcoupleapi.entity.Couple;
import com.fpt.locketcoupleapi.entity.ESex;
import com.fpt.locketcoupleapi.entity.EStatus;
import com.fpt.locketcoupleapi.entity.User;
import com.fpt.locketcoupleapi.exception.AppException;
import com.fpt.locketcoupleapi.exception.ErrorCode;
import com.fpt.locketcoupleapi.payload.DTO.CoupleDTO;
import com.fpt.locketcoupleapi.payload.DTO.UserDTO;
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
    public List<UserDTO> getMyCoupleByPending() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUserName(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        List<Couple> couples = new ArrayList<>();

        // Check user's sex and find couples accordingly
        if (user.getSex() == ESex.MALE || user.getSex() == ESex.FEMALE) {
            couples = coupleRepository.findCouplesByUserBoyfriend_UserId(user.getUserId());
        } else if (user.getSex() == ESex.OTHER) {
            couples = coupleRepository.findCouplesByUserBoyfriend_UserId(user.getUserId());
            if (couples.isEmpty()) {
                couples = coupleRepository.findCouplesByUserGirlfriend_UserId(user.getUserId());
            }
        }

        // Check if couples list is empty
        if (couples.isEmpty()) {
            throw new AppException(ErrorCode.COUPLE_NOT_FOUND);
        }

        List<UserDTO> userDTOs = new ArrayList<>();
        userDTOs.add(modelMapper.map(couples.get(0).getUserBoyfriend(), UserDTO.class));
        // Map each Couple to CoupleDTO
        return userDTOs;
    }


}
