package com.fpt.locketcoupleapi.service.impl;

import com.fpt.locketcoupleapi.entity.User;
import com.fpt.locketcoupleapi.exception.AppException;
import com.fpt.locketcoupleapi.exception.ErrorCode;
import com.fpt.locketcoupleapi.payload.response.FindUserResponse;
import com.fpt.locketcoupleapi.payload.response.MyInforUserResponse;
import com.fpt.locketcoupleapi.repository.UserRepository;
import com.fpt.locketcoupleapi.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public FindUserResponse findUserByUsername(String username) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        FindUserResponse response = new FindUserResponse();
        response.setFullName(user.getFullName());
        response.setUserName(user.getUserName());
        response.setAddress(user.getAddress());
        response.setPhone(user.getPhone());
        response.setEmail(user.getEmail());
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
        response.setPhone(user.getPhone());
        response.setEmail(user.getEmail());
        return response;
    }
}
