package com.fpt.locketcoupleapi.service;

public interface CoupleService {
        void sendRequest(int userId);

        void acceptRequest(int requestId);
}
