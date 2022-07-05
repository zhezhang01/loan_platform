package com.wwj.srb.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LendStatusEnum {

    CHECK(0, "To be released"),
    INVEST_RUN(1, "Fund raising"),
    PAY_RUN(2, "Repayment in progress"),
    PAY_OK(3, "Settled"),
    FINISH(4, "Bidding ends"),
    CANCEL(-1, "Bid cancellation"),
    ;

    private Integer status;
    private String msg;


    public static String getMsgByStatus(int status) {
        LendStatusEnum arrObj[] = LendStatusEnum.values();
        for (LendStatusEnum obj : arrObj) {
            if (status == obj.getStatus().intValue()) {
                return obj.getMsg();
            }
        }
        return "";
    }
}
