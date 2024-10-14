package com.fpt.locketcoupleapi.service;


import com.cloudinary.Api;
import com.fpt.locketcoupleapi.payload.request.AuthenticationRequest;
import com.fpt.locketcoupleapi.payload.request.IntrospectRequest;
import com.fpt.locketcoupleapi.payload.request.RefreshTokenRequest;
import com.fpt.locketcoupleapi.payload.request.SignUpRequest;
import com.fpt.locketcoupleapi.payload.response.ApiResponse;
import com.fpt.locketcoupleapi.payload.response.AuthenticationResponse;
import com.fpt.locketcoupleapi.payload.response.IntrospectResponse;
import com.fpt.locketcoupleapi.payload.response.SignUpResponse;
import com.nimbusds.jose.JOSEException;
import jakarta.mail.MessagingException;

import java.text.ParseException;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);

    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;

    ApiResponse<AuthenticationResponse> refreshToken(RefreshTokenRequest refreshTokenRequest) throws ParseException, JOSEException;

    ApiResponse<SignUpResponse> signUp(SignUpRequest signUpRequest);


}
