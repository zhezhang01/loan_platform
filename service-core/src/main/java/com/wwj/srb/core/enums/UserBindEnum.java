package com.wwj.srb.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
//@ToString
public enum UserBindEnum {

    NO_BIND(0, "Unbound"),
    BIND_OK(1, "Bound"),
    BIND_FAIL(-1, "Binding failed"),
    ;

    private Integer status;
    private String msg;
}
