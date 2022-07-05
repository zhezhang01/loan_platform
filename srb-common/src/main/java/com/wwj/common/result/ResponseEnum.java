package com.wwj.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum ResponseEnum {

    SUCCESS(0, "Success"),
    ERROR(-1, "Server internal error"),


    //-1xx server error
    BAD_SQL_GRAMMAR_ERROR(-101, "sql error"),
    SERVLET_ERROR(-102, "servlet request exception"), //-2xx
    UPLOAD_ERROR(-103, "Fail to upload the file"),
    EXPORT_DATA_ERROR(104, "Fail to export the data"),


    //-2xx
    BORROW_AMOUNT_NULL_ERROR(-201, "Loan amount cannot be blank"),
    MOBILE_NULL_ERROR(-202, "Phone number cannot be empty"),
    MOBILE_ERROR(-203, "Incorrect phone number"),
    PASSWORD_NULL_ERROR(204, "Password cannot be empty"),
    CODE_NULL_ERROR(205, "Verification code cannot be empty"),
    CODE_ERROR(206, "Wrong verification code"),
    MOBILE_EXIST_ERROR(207, "Phone number has been registered"),
    LOGIN_MOBILE_ERROR(208, "User didn't exist"),
    LOGIN_PASSWORD_ERROR(209, "Wrong password"),
    LOGIN_LOKED_ERROR(210, "The user has been locked down"),
    LOGIN_AUTH_ERROR(-211, "Not signed in"),


    USER_BIND_IDCARD_EXIST_ERROR(-301, "ID number has been bound"),
    USER_NO_BIND_ERROR(302, "User not bound"),
    USER_NO_AMOUNT_ERROR(303, "User information not reviewed"),
    USER_AMOUNT_LESS_ERROR(304, "Your loan limit is insufficient"),
    LEND_INVEST_ERROR(305, "Unable to bid in current status"),
    LEND_FULL_SCALE_ERROR(306, "Full bid, unable to bid！"),
    NOT_SUFFICIENT_FUNDS_ERROR(307, "Insufficient balance, please recharge"),

    PAY_UNIFIEDORDER_ERROR(401, "Unified order placing error"),

    ALIYUN_RESPONSE_ERRPR(-501, "Server SMS service response failed"),
    ALIYUN_SMS_LIMIT_CONTROL_ERROR(-502, "SMS is sent too frequently"),
    ALIYUN_SMS_ERROR(-503, "Fail to send the message"),//Other

    WEIXIN_CALLBACK_PARAM_ERROR(-601, "Incorrect callback parameters"),
    WEIXIN_FETCH_ACCESSTOKEN_ERROR(-602, "Fail to get access_token"),
    WEIXIN_FETCH_USERINFO_ERROR(-603, "Failed to get user information");

    /**
     * Response code
     */
    private Integer code;
    /**
     * Response information
     */
    private String message;
}
