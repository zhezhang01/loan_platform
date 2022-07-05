package com.wwj.srb.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransTypeEnum {
    RECHARGE(1,"Recharge"),
    INVEST_LOCK(2,"Bid locking"),
    INVEST_UNLOCK(3,"Loan unlocking"),
    CANCEL_LEND(4,"Withdrawal of bid"),
    BORROW_BACK(5,"Loan arrival"),
    RETURN_DOWN(6,"repayment deduction"),
    INVEST_BACK(7,"Loan collection"),
    WITHDRAW(8,"withdraw"),
    ;

    private Integer transType ;
    private String transTypeName;


    public static String getTransTypeName(int transType) {
        for (TransTypeEnum obj : TransTypeEnum.values()) {
            if (transType == obj.getTransType().intValue()) {
                return obj.getTransTypeName();
            }
        }
        return "";
    }

}
