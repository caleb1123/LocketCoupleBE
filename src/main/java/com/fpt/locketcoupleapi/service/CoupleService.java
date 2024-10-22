package com.fpt.locketcoupleapi.service;

import com.fpt.locketcoupleapi.payload.DTO.CoupleDTO;
import com.fpt.locketcoupleapi.payload.DTO.UserDTO;
import com.fpt.locketcoupleapi.payload.response.CoupleResponse;
import com.fpt.locketcoupleapi.payload.response.SendRequestResponse;

import java.util.List;

public interface CoupleService {
        void sendRequest(int userId);

        void acceptRequest(int requestId);

        List<CoupleDTO> getAllCouple();

        SendRequestResponse getMyCoupleByPending();

        void CancelRequest(int requestId);

        SendRequestResponse getSendRequest();

        CoupleResponse getMyCoupleByComplet();
}
