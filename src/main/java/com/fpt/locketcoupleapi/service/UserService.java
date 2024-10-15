package com.fpt.locketcoupleapi.service;

import com.fpt.locketcoupleapi.payload.response.FindUserResponse;

public interface UserService {
    FindUserResponse findUserByUsername(String username);
}
