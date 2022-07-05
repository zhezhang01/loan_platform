package com.wwj.srb.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum IntegralEnum {

    BORROWER_IDCARD(30, "Borrower's id information"),
    BORROWER_HOUSE(100, "Borrower's house information"),
    BORROWER_CAR(60, "Borrower's vehicle information"),
    ;

    private Integer integral;
    private String msg;
}
