package com.fpt.locketcoupleapi.service;

import com.fpt.locketcoupleapi.payload.DTO.CoupleDTO;
import com.fpt.locketcoupleapi.payload.DTO.UserDTO;

import java.util.List;

public interface CoupleService {
        void sendRequest(int userId);

        void acceptRequest(int requestId);

        List<CoupleDTO> getAllCouple();

        List<UserDTO> getMyCoupleByPending();
}
