package com.fpt.locketcoupleapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid key", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User name not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_CORRECT(1009, "Incorrect password", HttpStatus.UNAUTHORIZED),
    EMAIL_TAKEN(1010, "Email already in use", HttpStatus.BAD_REQUEST),
    EMPTY_FIELD(1011, "You cannot leave required field(s) empty", HttpStatus.BAD_REQUEST),
    ITEM_NOT_FOUND(1012, "Item not found", HttpStatus.NOT_FOUND),
    PHONE_TAKEN(1013, "Phone number already in use", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(1014, "JWT ID is null", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_EXISTED(1015, "Email not existed", HttpStatus.NOT_FOUND),
    ID_EXISTED(1016, "Id already exists", HttpStatus.BAD_REQUEST),
    ID_NOT_MATCHED(1017, "Id does not match request", HttpStatus.BAD_REQUEST),
    POST_CATEGORY_NOT_FOUND(1018, "Post category not found", HttpStatus.NOT_FOUND),
    BID_NOT_FOUND(1019, "Bid not found", HttpStatus.NOT_FOUND),
    AUCTION_NOT_FOUND(1020, "Auction not found", HttpStatus.NOT_FOUND),
    AUCTION_NOT_CLOSED(1021, "Auction has not closed yet", HttpStatus.BAD_REQUEST),
    AUCTION_CLOSED(1022, "Auction is not open yet or has already closed", HttpStatus.BAD_REQUEST),
    INVALID_BID(1023, "Bid must be higher than previous one or the current highest bid", HttpStatus.BAD_REQUEST),
    REQUEST_EXISTED(1024, "Request for this jewelry already exists", HttpStatus.BAD_REQUEST),
    JEWELRY_NOT_EXISTED(1025,"Jewelry not found",HttpStatus.NOT_FOUND),
    JEWELRY_NOT_VALID(1026,"Jewelry not valid for auction",HttpStatus.NOT_FOUND),
    NOT_LOGGED_IN(1027,"You need to log in to perform this action",HttpStatus.BAD_REQUEST),
    INVALID_VALUE(1028,"Input value must be greater than 0",HttpStatus.BAD_REQUEST),
    INVALID_STARTDATE(1029,"Start date cannot be after end date",HttpStatus.BAD_REQUEST),
    INVALID_ENDDATE(1030,"End date cannot be before current date",HttpStatus.BAD_REQUEST),
    LIST_EMPTY(1031,"List is empty",HttpStatus.NOT_FOUND),
    REQUEST_NOT_FOUND(1032, "Request not found", HttpStatus.NOT_FOUND),
    POST_NOT_FOUND(1033,"Post not found",HttpStatus.BAD_REQUEST ),
    INVALID_STATUS(1034,"Invalid status",HttpStatus.BAD_REQUEST ),
    NO_IMAGE_FOUND(1035,"Image not found",HttpStatus.NOT_FOUND),
    IMAGE_MANY(1036,"Too many photos",HttpStatus.BAD_REQUEST),
    USERNAME_EXISTED(1037,"Username already existed",HttpStatus.BAD_REQUEST),
    INVALID_AGE(1038,"User must be 18 years old or over",HttpStatus.BAD_REQUEST),
    HIGHEST_BIDDER_CANNOT_BID_AGAIN(1039,"You are the highest bidder",HttpStatus.BAD_REQUEST),
    AUCTION_NOT_ONGOING(1040,"Auction is not ongoing",HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(1042,"Role not found",HttpStatus.NOT_FOUND),
    ACCOUNT_INACTIVE(1041, "Account has not been activated", HttpStatus.FORBIDDEN),
    USER_NOT_ACTIVE(101, "User is not active", HttpStatus.FORBIDDEN),
    COUPLE_NOT_FOUND(1043, "Couple not found", HttpStatus.NOT_FOUND),
    MY_INFOR(1044, "you can't make friends with yourself", HttpStatus.BAD_REQUEST),
    COUPLE_EXISTED(1045, "Couple existed", HttpStatus.BAD_REQUEST),
    COUPLE_NOT_EXISTED(1046, "Couple not existed", HttpStatus.BAD_REQUEST),
    PHOTO_NOT_EXISTED(1047, "Photo not existed", HttpStatus.BAD_REQUEST);


    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
