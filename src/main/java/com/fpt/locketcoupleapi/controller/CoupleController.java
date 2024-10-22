package com.fpt.locketcoupleapi.controller;

import com.cloudinary.Api;
import com.fpt.locketcoupleapi.exception.AppException;
import com.fpt.locketcoupleapi.payload.DTO.CoupleDTO;
import com.fpt.locketcoupleapi.payload.DTO.UserDTO;
import com.fpt.locketcoupleapi.payload.response.ApiResponse;
import com.fpt.locketcoupleapi.payload.response.CoupleResponse;
import com.fpt.locketcoupleapi.payload.response.SendRequestResponse;
import com.fpt.locketcoupleapi.service.CoupleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/acceptRequest/{requestId}")
    public ResponseEntity<ApiResponse<String>> acceptRequest(@PathVariable int requestId) {
        try {
            // Gọi service để chấp nhận yêu cầu kết bạn
            coupleService.acceptRequest(requestId);

            // Tạo phản hồi thành công
            ApiResponse<String> response = ApiResponse.<String>builder()
                    .code(HttpStatus.OK.value())
                    .message("Accepted request successfully")
                    .data(null) // Không có dữ liệu trả về
                    .build();

            // Trả về phản hồi thành công
            return ResponseEntity.ok(response);
        } catch (AppException e) {
            // Tạo phản hồi khi gặp lỗi ứng dụng, ví dụ: yêu cầu không tồn tại
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

    @GetMapping("/getAllCouple")
    public ResponseEntity<ApiResponse<List<CoupleDTO>>> getAllCouple() {
        try {
            // Gọi service để lấy danh sách tất cả cặp đôi
            List<CoupleDTO> couple =coupleService.getAllCouple();

            // Tạo phản hồi thành công
            ApiResponse<List<CoupleDTO>> response = ApiResponse.<List<CoupleDTO>>builder()
                    .code(HttpStatus.OK.value())
                    .message("Get all couple successfully")
                    .data(couple) // Không có dữ liệu trả về
                    .build();

            // Trả về phản hồi thành công
            return ResponseEntity.ok(response);
        } catch (AppException e) {
            // Tạo phản hồi khi gặp lỗi ứng dụng, ví dụ: không có cặp đôi nào
            ApiResponse<List<CoupleDTO>> errorResponse = ApiResponse.<List<CoupleDTO>>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            // Tạo phản hồi khi gặp lỗi không xác định
            ApiResponse<List<CoupleDTO>> errorResponse = ApiResponse.<List<CoupleDTO>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("An error occurred")
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/CoupleInvite")
    public ResponseEntity<ApiResponse<SendRequestResponse>> getMyCouple() {
        try {
            // Get the couple data from the service
            SendRequestResponse coupleDTOs = coupleService.getMyCoupleByPending();

            // Create a successful response
            ApiResponse<SendRequestResponse> response = ApiResponse.<SendRequestResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message("Couple(s) found")
                    .data(coupleDTOs)
                    .build();

            return ResponseEntity.ok(response);

        } catch (AppException e) {
            // Handle known exceptions (e.g. user not found or couple not found)
            ApiResponse<SendRequestResponse> errorResponse = ApiResponse.<SendRequestResponse>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

        } catch (Exception e) {
            // Handle generic errors
            ApiResponse<SendRequestResponse> errorResponse = ApiResponse.<SendRequestResponse>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("An unexpected error occurred")
                    .data(null)
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/CancelRequest/{requestId}")
    public ResponseEntity<ApiResponse<String>> CancelRequest(@PathVariable int requestId) {
        try {
            // Gọi service để hủy yêu cầu kết bạn
            coupleService.CancelRequest(requestId);

            // Tạo phản hồi thành công
            ApiResponse<String> response = ApiResponse.<String>builder()
                    .code(HttpStatus.OK.value())
                    .message("Cancel request successfully")
                    .data(null) // Không có dữ liệu trả về
                    .build();

            // Trả về phản hồi thành công
            return ResponseEntity.ok(response);
        } catch (AppException e) {
            // Tạo phản hồi khi gặp lỗi ứng dụng, ví dụ: yêu cầu không tồn tại
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

    @GetMapping("/getSendRequest")
    public ResponseEntity<ApiResponse<SendRequestResponse>> getSendRequest() {
        try {
            // Get the couple data from the service
            SendRequestResponse coupleDTOs = coupleService.getSendRequest();

            // Create a successful response
            ApiResponse<SendRequestResponse> response = ApiResponse.<SendRequestResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message("Couple(s) found")
                    .data(coupleDTOs)
                    .build();

            return ResponseEntity.ok(response);

        } catch (AppException e) {
            // Handle known exceptions (e.g. user not found or couple not found)
            ApiResponse<SendRequestResponse> errorResponse = ApiResponse.<SendRequestResponse>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

        } catch (Exception e) {
            // Handle generic errors
            ApiResponse<SendRequestResponse> errorResponse = ApiResponse.<SendRequestResponse>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("An unexpected error occurred")
                    .data(null)
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/getMyLover")
    public ResponseEntity<ApiResponse<CoupleResponse>> getMyLover() {
        try {
            // Gọi service để lấy thông tin couple
            CoupleResponse couple = coupleService.getMyCoupleByComplet();

            // Nếu thành công, trả về ApiResponse với dữ liệu couple
            ApiResponse<CoupleResponse> response = ApiResponse.<CoupleResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message("Couple found successfully")
                    .data(couple)
                    .build();

            return ResponseEntity.ok(response);

        } catch (AppException e) {
            // Xử lý ngoại lệ do lỗi nghiệp vụ và trả về lỗi với mã code tương ứng
            ApiResponse<CoupleResponse> errorResponse = ApiResponse.<CoupleResponse>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .data(null) // Không trả về dữ liệu khi có lỗi
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        } catch (Exception e) {
            // Xử lý ngoại lệ không mong muốn
            ApiResponse<CoupleResponse> errorResponse = ApiResponse.<CoupleResponse>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("An unexpected error occurred")
                    .data(null)
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
