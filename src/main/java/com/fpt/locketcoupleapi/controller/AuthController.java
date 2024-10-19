package com.fpt.locketcoupleapi.controller;

import com.fpt.locketcoupleapi.exception.AppException;
import com.fpt.locketcoupleapi.payload.request.AuthenticationRequest;
import com.fpt.locketcoupleapi.payload.request.SignUpRequest;
import com.fpt.locketcoupleapi.payload.response.ApiResponse;
import com.fpt.locketcoupleapi.payload.response.AuthenticationResponse;
import com.fpt.locketcoupleapi.payload.response.SignUpResponse;
import com.fpt.locketcoupleapi.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
@CrossOrigin("http://localhost:3000")
public class AuthController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(@RequestBody AuthenticationRequest request) {
        try {
            // Attempt to authenticate the user
            AuthenticationResponse authenticationResponse = authenticationService.authenticate(request);

            // Create success response
            ApiResponse<AuthenticationResponse> response = ApiResponse.<AuthenticationResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message("Login successful")
                    .data(authenticationResponse)
                    .build();

            return ResponseEntity.ok(response);

        } catch (AppException e) {
            // Handle known exceptions and return a response with the same type
            ApiResponse<AuthenticationResponse> errorResponse = ApiResponse.<AuthenticationResponse>builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message(e.getMessage())
                    .data(null) // Keeping the data field null as per requirement
                    .build();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);

        } catch (Exception e) {
            // Handle generic exceptions
            ApiResponse<AuthenticationResponse> errorResponse = ApiResponse.<AuthenticationResponse>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("An unexpected error occurred")
                    .data(null)
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }




    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignUpResponse>> signUp(@RequestBody SignUpRequest signUpRequest) {
        try {
            // Gọi service để thực hiện đăng ký
            ApiResponse<SignUpResponse> response = authenticationService.signUp(signUpRequest);

            // Trả về phản hồi với mã trạng thái 201 (Created) nếu đăng ký thành công
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (AppException e) {
            // Xử lý lỗi ứng dụng, ví dụ: tên người dùng đã tồn tại
            ApiResponse<SignUpResponse> errorResponse = ApiResponse.<SignUpResponse>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .data(null) // Không có dữ liệu trả về khi có lỗi
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            // Xử lý các lỗi không xác định khác
            ApiResponse<SignUpResponse> errorResponse = ApiResponse.<SignUpResponse>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("An error occurred during signup")
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


}
