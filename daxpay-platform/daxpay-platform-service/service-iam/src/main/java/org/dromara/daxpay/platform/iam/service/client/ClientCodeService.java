package org.dromara.daxpay.platform.iam.service.client;

import org.dromara.daxpay.platform.common.request.context.RequestContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 获取终端编码
///
@Slf4j
@Service
public class ClientCodeService {

    /// 获取终端编码
    public String getClientCode(){
        return RequestContextHolder.getClientCode();
    }
}

