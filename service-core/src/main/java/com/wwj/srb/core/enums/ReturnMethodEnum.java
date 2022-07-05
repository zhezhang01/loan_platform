package com.wwj.srb.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReturnMethodEnum {

    ONE(1, "Equal principal and interest"),
    TWO(2, "Equivalent principal"),
    THREE(3, "Repay interest and principal once a month"),
    FOUR(4, "One time repayment of principal and interest"),
    ;

    private Integer method;
    private String msg;
}
