package com.wwj.srb.sms.client.follback;

import com.wwj.srb.sms.client.CoreUserInfoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * When the remote service call fails, execute the alternative scheme
 */
@Service
@Slf4j
public class CoreUserInfoClientFallback implements CoreUserInfoClient {

    /**
     * When the upstream service (verifying whether the mobile phone number has been registered) is blown, execute this alternative scheme
     *
     * @param mobile
     * @return
     */
    @Override
    public boolean checkMobile(String mobile) {
        log.error("Remote invoke failed, service blown......");
        return false;
    }
}
