package com.fpt.locketcoupleapi.controller;

import com.fpt.locketcoupleapi.entity.User;
import com.fpt.locketcoupleapi.exception.AppException;
import com.fpt.locketcoupleapi.payload.DTO.UserDTO;
import com.fpt.locketcoupleapi.payload.request.AuthenticationRequest;
import com.fpt.locketcoupleapi.payload.request.UserUpdateResquest;
import com.fpt.locketcoupleapi.payload.response.ApiResponse;
import com.fpt.locketcoupleapi.payload.response.AuthenticationResponse;
import com.fpt.locketcoupleapi.payload.response.FindUserResponse;
import com.fpt.locketcoupleapi.payload.response.MyInforUserResponse;
import com.fpt.locketcoupleapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
@CrossOrigin("http://localhost:3000")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/find-by-user")
    public ResponseEntity<ApiResponse<FindUserResponse>> login(@RequestParam String username) {
        try {
            // Attempt to authenticate the user
            FindUserResponse user = userService.findUserByUsername(username);

            // Create success response
            ApiResponse<FindUserResponse> response = ApiResponse.<FindUserResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message("User found")
                    .data(user)
                    .build();

            return ResponseEntity.ok(response);

        } catch (AppException e) {
            // Handle known exceptions and return a response with the same type
            ApiResponse<FindUserResponse> errorResponse = ApiResponse.<FindUserResponse>builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message(e.getMessage())
                    .data(null) // Keeping the data field null as per requirement
                    .build();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);

        } catch (Exception e) {
            // Handle generic exceptions
            ApiResponse<FindUserResponse> errorResponse = ApiResponse.<FindUserResponse>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("An unexpected error occurred")
                    .data(null)
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/my-profile")
    public ResponseEntity<ApiResponse<MyInforUserResponse>> myProfile() {
        try {
            // Attempt to authenticate the user
            MyInforUserResponse user = userService.getMyInfor();

            // Create success response
            ApiResponse<MyInforUserResponse> response = ApiResponse.<MyInforUserResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message("User found")
                    .data(user)
                    .build();

            return ResponseEntity.ok(response);

        } catch (AppException e) {
            // Handle known exceptions and return a response with the same type
            ApiResponse<MyInforUserResponse> errorResponse = ApiResponse.<MyInforUserResponse>builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message(e.getMessage())
                    .data(null) // Keeping the data field null as per requirement
                    .build();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);

        } catch (Exception e) {
            // Handle generic exceptions
            ApiResponse<MyInforUserResponse> errorResponse = ApiResponse.<MyInforUserResponse>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("An unexpected error occurred")
                    .data(null)
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<UserDTO>> updateProfile(@RequestBody UserUpdateResquest user) {
        try {
            // Attempt to authenticate the user
            UserDTO updatedUser = userService.updateMyInfor(user);

            // Create success response
            ApiResponse<UserDTO> response = ApiResponse.<UserDTO>builder()
                    .code(HttpStatus.OK.value())
                    .message("User updated")
                    .data(updatedUser)
                    .build();

            return ResponseEntity.ok(response);

        } catch (AppException e) {
            // Handle known exceptions and return a response with the same type
            ApiResponse<UserDTO> errorResponse = ApiResponse.<UserDTO>builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message(e.getMessage())
                    .data(null) // Keeping the data field null as per requirement
                    .build();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);

        } catch (Exception e) {
            // Handle generic exceptions
            ApiResponse<UserDTO> errorResponse = ApiResponse.<UserDTO>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("An unexpected error occurred")
                    .data(null)
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }



}
