package com.fpt.locketcoupleapi.controller;

import com.fpt.locketcoupleapi.exception.AppException;
import com.fpt.locketcoupleapi.payload.response.ApiResponse;
import com.fpt.locketcoupleapi.service.CoupleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/couple")
@Slf4j
@CrossOrigin("http://localhost:3000")
public class CoupleController {
    @Autowired
    private CoupleService coupleService;

    @PostMapping("/sendRequest/{userId}")
    public ResponseEntity<ApiResponse<String>> sendRequest(@PathVariable int userId) {
        try {
            // Gọi service để gửi yêu cầu kết bạn
            coupleService.sendRequest(userId);

            // Tạo phản hồi thành công
            ApiResponse<String> response = ApiResponse.<String>builder()
                    .code(HttpStatus.OK.value())
                    .message("Sent request successfully")
                    .data(null) // Không có dữ liệu trả về
                    .build();

            // Trả về phản hồi thành công
            return ResponseEntity.ok(response);
        } catch (AppException e) {
            // Tạo phản hồi khi gặp lỗi ứng dụng, ví dụ: người dùng không tồn tại
            ApiResponse<String> errorResponse = ApiResponse.<String>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            // Tạo phản hồi khi gặp lỗi không xác định
            ApiResponse<String> errorResponse = ApiResponse.<String>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("An error occurred")
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

}
