package com.fpt.locketcoupleapi.service;

import com.fpt.locketcoupleapi.payload.response.FindUserResponse;
import com.fpt.locketcoupleapi.payload.response.MyInforUserResponse;

public interface UserService {
    FindUserResponse findUserByUsername(String username);

    MyInforUserResponse getMyInfor();

}
