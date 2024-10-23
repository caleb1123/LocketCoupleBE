package com.fpt.locketcoupleapi.controller;

import com.fpt.locketcoupleapi.exception.AppException;
import com.fpt.locketcoupleapi.payload.response.ApiResponse;
import com.fpt.locketcoupleapi.service.PhotoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/photo")
@Slf4j
@CrossOrigin("http://localhost:3000")
public class PhotoController {
    @Autowired
    private PhotoService photoService;

    @PostMapping("/uploadFileWithCouple/")
    public ResponseEntity<ApiResponse<String>> uploadFileWithCouple(@RequestParam("file") MultipartFile file) {
        try {
            // Gọi service để upload file liên quan đến couple
            String resultMessage = photoService.uploadFileWithCouple(file);

            // Tạo phản hồi thành công
            ApiResponse<String> response = ApiResponse.<String>builder()
                    .code(HttpStatus.OK.value())
                    .message(resultMessage)
                    .data(null) // Không có dữ liệu trả về
                    .build();

            // Trả về phản hồi thành công
            return ResponseEntity.ok(response);
        } catch (AppException e) {
            // Tạo phản hồi khi gặp lỗi ứng dụng, ví dụ: couple không tồn tại
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
                    .message("An error occurred while uploading the file")
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


}
