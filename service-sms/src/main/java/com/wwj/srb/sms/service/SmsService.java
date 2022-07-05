package com.wwj.srb.sms.service;

import java.util.Map;

public interface SmsService {

    /**
     * Send verification code SMS
     * @param mobile
     * @param templateCode
     * @param param
     */
    void send(String mobile, String templateCode, Map<String,Object> param);
}
