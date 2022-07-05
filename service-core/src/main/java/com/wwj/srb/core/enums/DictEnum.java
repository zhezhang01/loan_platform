package com.wwj.srb.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DictEnum {

    EDUCATION("education", "Education"),
    INDUSTRY("industry", "Industry"),
    INCOME("income", "Source of income"),
    RETURN_SOURCE("returnSource", "Source of repayment"),
    RELATION("relation", "Relationship with contacts"),

    RETURN_METHOD("returnMethod", "Repayment way"),
    MONEY_USER("moneyUse", "Purpose of funds"),
    ;

    private String dictCode;
    private String msg;
}
