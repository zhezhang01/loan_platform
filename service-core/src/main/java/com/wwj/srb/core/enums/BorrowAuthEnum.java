package com.wwj.srb.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BorrowAuthEnum {

    NO_AUTH(0, "Not certified"),
    AUTH_RUN(1, "under certification"),
    AUTH_OK(2, "Authentication succeeded"),
    AUTH_FAIL(-1, "Authentication failed"),
    ;

    private Integer status;
    private String msg;
}
