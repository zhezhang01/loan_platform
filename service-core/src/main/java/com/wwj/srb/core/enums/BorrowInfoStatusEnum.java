package com.wwj.srb.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BorrowInfoStatusEnum {

    NO_AUTH(0, "Not certified"),
    CHECK_RUN(1, "Under review"),
    CHECK_OK(2, "Audit passed"),
    CHECK_FAIL(-1, "Audit not passed"),
    ;

    private Integer status;
    private String msg;

    public static String getMsgByStatus(int status) {
        BorrowInfoStatusEnum arrObj[] = BorrowInfoStatusEnum.values();
        for (BorrowInfoStatusEnum obj : arrObj) {
            if (status == obj.getStatus().intValue()) {
                return obj.getMsg();
            }
        }
        return "";
    }
}
