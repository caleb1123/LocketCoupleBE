package com.fpt.locketcoupleapi.service.impl;


import com.fpt.locketcoupleapi.entity.User;
import com.fpt.locketcoupleapi.exception.AppException;
import com.fpt.locketcoupleapi.exception.ErrorCode;
import com.fpt.locketcoupleapi.payload.request.AuthenticationRequest;
import com.fpt.locketcoupleapi.payload.request.IntrospectRequest;
import com.fpt.locketcoupleapi.payload.request.RefreshTokenRequest;
import com.fpt.locketcoupleapi.payload.request.SignUpRequest;
import com.fpt.locketcoupleapi.payload.response.ApiResponse;
import com.fpt.locketcoupleapi.payload.response.AuthenticationResponse;
import com.fpt.locketcoupleapi.payload.response.IntrospectResponse;
import com.fpt.locketcoupleapi.payload.response.SignUpResponse;
import com.fpt.locketcoupleapi.repository.UserRepository;
import com.fpt.locketcoupleapi.service.AuthenticationService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.mail.MessagingException;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    UserRepository accountRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${app.jwt-secret}")
    private String SIGNER_KEY;

    @Value(value = "${app.jwt-access-expiration-milliseconds}")
    private long VALID_DURATION;

    @Value("${app.jwt-refresh-expiration-milliseconds}")
    private long REFRESHABLE_DURATION;


    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        // Tìm người dùng từ username
        User user = accountRepository
                .findByUserName(request.getUserName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Kiểm tra trạng thái kích hoạt của người dùng
        if (!user.isActive()) {
            throw new AppException(ErrorCode.USER_NOT_ACTIVE);
        }

        // Xác thực mật khẩu
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new AppException(ErrorCode.PASSWORD_NOT_CORRECT);
        }

        // Tạo token
        var token = generateToken(user);

        // Tạo phản hồi thành công
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }



    @Override
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();


        boolean isValid = true;
        try {
            verifyToken(token,false);
        } catch (AppException e){
            isValid = false;
        }

        return IntrospectResponse.builder().valid(isValid).build();
    }

    @Override
    public ApiResponse<AuthenticationResponse> refreshToken(RefreshTokenRequest refreshTokenRequest) {
        try {
            var signedJWT = verifyToken(refreshTokenRequest.getToken(), true);

            // Lấy thông tin từ token
            var jit = signedJWT.getJWTClaimsSet().getJWTID();
            var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            var username = signedJWT.getJWTClaimsSet().getSubject();

            // Tìm người dùng từ username
            var user = accountRepository.findByUserName(username)
                    .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

            // Tạo token mới
            var token = generateToken(user);

            // Tạo phản hồi thành công
            AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                    .token(token)
                    .authenticated(true)
                    .build();

            return ApiResponse.<AuthenticationResponse>builder()
                    .code(200) // Mã trạng thái thành công
                    .message("Token refreshed successfully")
                    .data(authenticationResponse)
                    .build();
        } catch (AppException e) {
            // Xử lý ngoại lệ và trả về phản hồi lỗi
            return ApiResponse.<AuthenticationResponse>builder()
                    .code(e.getErrorCode().getCode()) // Giả sử bạn có phương thức getCode() trong ErrorCode
                    .message(e.getMessage())
                    .build();
        } catch (ParseException | JOSEException e) {
            // Xử lý các ngoại lệ liên quan đến token
            return ApiResponse.<AuthenticationResponse>builder()
                    .code(400) // Mã lỗi cho yêu cầu không hợp lệ
                    .message("Invalid token")
                    .build();
        } catch (Exception e) {
            // Xử lý các ngoại lệ khác (nếu cần)
            return ApiResponse.<AuthenticationResponse>builder()
                    .code(500) // Mã lỗi nội bộ
                    .message("An unexpected error occurred")
                    .build();
        }
    }

    @Override
    public ApiResponse<SignUpResponse> signUp(SignUpRequest signUpRequest) {
        // Kiểm tra xem tên đăng nhập đã tồn tại hay chưa
        if (accountRepository.findByUserName(signUpRequest.getUserName()).isPresent()) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }
        User newUser = modelMapper.map(signUpRequest, User.class);

        // Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        newUser.setPassword(encodedPassword);
        newUser.setActive(true);
        // Lưu người dùng mới vào cơ sở dữ liệu
        accountRepository.save(newUser);

        // Tạo phản hồi cho đăng ký thành công
        SignUpResponse signUpResponse = SignUpResponse.builder()
                .userId(newUser.getUserId())
                .fullName(newUser.getFullName())
                .userName(newUser.getUserName())
                .address(newUser.getAddress())
                .dob(newUser.getDob())
                .email(newUser.getEmail())
                .sex(String.valueOf(newUser.getSex()))
                .phone(newUser.getPhone())
                .createdDate(newUser.getCreatedDate())
                .updatedDate(newUser.getUpdatedDate())
                .active(newUser.isActive())
                .build();

        // Trả về phản hồi API
        return ApiResponse.<SignUpResponse>builder()
                .code(200) // Mã trạng thái thành công
                .message("User registered successfully.")
                .data(signUpResponse)
                .build();
    }


//    @Transactional
//    @Override
//    public void generateAndSendOtp(String email) throws MessagingException {
//        var account = accountRepository.findByEmail(email).orElseThrow(
//                () -> new AppException(ErrorCode.EMAIL_NOT_EXISTED)
//        );
//
//        String otp = String.valueOf(100000 + new Random().nextInt(900000)); // Generate a 6-digit OTP
//        OTPToken otpToken = new OTPToken(); // OTP expires in 15 minutes
//        otpToken.setEmail(email);
//        otpToken.setOtp(otp);
//        otpToken.setExpiryDate(Instant.now().plus(15,ChronoUnit.MINUTES));
//
//        otpTokenRepository.save(otpToken);
//        emailService.sendResetPasswordEmail(email,otp,account.getFullName());
//    }

//    @Override
//    public void resetPasswordWithOtp(String email, String otp, String newPassword) {
//        OTPToken otpToken = otpTokenRepository.findByEmailAndOtp(email, otp);
//        if (otpToken == null || otpToken.isExpired()) {
//            throw new IllegalArgumentException("Invalid or expired OTP");
//        }
//
//        var account = accountRepository.findByEmail(email).orElseThrow(
//                () -> new AppException(ErrorCode.EMAIL_NOT_EXISTED)
//        );
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
//
//        // Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
//        String encodedPassword = passwordEncoder.encode(newPassword);
//        account.setPassword(encodedPassword);
//        accountRepository.save(account);
//
//        otpTokenRepository.delete(otpToken);
//    }


    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh)
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime()
                .toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }

    private String generateToken(User account) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(account.getUserName())
                .issuer("Mercury.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
//                .claim("scope", buildScope(account))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
                throw new RuntimeException(e);
        }
    }

//    private String buildScope(User account) {
//        Role role = account.getRole();
//        if (role != null) {
//            return role.getRoleName().name(); // Assuming roleName is an enum or string
//        }
//        return "";
//    }

//    @Override
//    public void activeAccountWithOTP(String email, String otp) {
//        OTPToken otpToken = otpTokenRepository.findByEmailAndOtp(email, otp);
//        if (otpToken == null || otpToken.isExpired()) {
//            throw new IllegalArgumentException("Invalid or expired OTP");
//        }
//
//        var account = accountRepository.findByEmail(email).orElseThrow(
//                () -> new AppException(ErrorCode.EMAIL_NOT_EXISTED)
//        );
//
//        account.setStatus(true);
//
//        accountRepository.save(account);
//
//        otpTokenRepository.delete(otpToken);
//    }
//    @Transactional
//    @Override
//    public void generateAndSendOtpforActive(String email) throws MessagingException {
//        var account = accountRepository.findByEmail(email).orElseThrow(
//                () -> new AppException(ErrorCode.EMAIL_NOT_EXISTED)
//        );
//
//        String otp = String.valueOf(100000 + new Random().nextInt(900000)); // Generate a 6-digit OTP
//        OTPToken otpToken = new OTPToken(); // OTP expires in 15 minutes
//        otpToken.setEmail(email);
//        otpToken.setOtp(otp);
//        otpToken.setExpiryDate(Instant.now().plus(15,ChronoUnit.MINUTES));
//
//        otpTokenRepository.save(otpToken);
//        emailService.sendOTPtoActiveAccount(email,otp,account.getFullName());
//    }
//
//
//    @Override
//    public AccountDTO createAccountByUser(SignUpRequest signUpRequest) {
//        Account existingUserEmail = accountRepository.findByEmail(signUpRequest.getEmail()).orElse(null);
//        Account existingUserPhone = accountRepository.findByPhone(signUpRequest.getPhone()).orElse(null);
//        Account existingUserName = accountRepository.findByUserName(signUpRequest.getUserName()).orElse(null);
//        if (existingUserEmail != null) {
//            throw new AppException(ErrorCode.EMAIL_TAKEN);
//        }
//        if (existingUserPhone != null) {
//            throw new AppException(ErrorCode.PHONE_TAKEN);
//        }
//        if (existingUserName != null) {
//            throw new AppException(ErrorCode.USERNAME_EXISTED);
//        }
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
//        // Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
//        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
//        signUpRequest.setPassword(encodedPassword);
//
//        Account createAccount = accountConverter.toEntity(signUpRequest);
//
//        Role userRole = roleRepository.findById(4)
//                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//
//        createAccount.setRole(userRole);
//        createAccount.setStatus(false);
//
//        accountRepository.save(createAccount);
//
//        return accountConverter.toDTO(createAccount);
//    }
        public  void print(){
            System.out.println("Hello");
        }
}
