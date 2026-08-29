package cn.daxpay.open.platform.iam.service.client;

import cn.daxpay.open.platform.common.request.context.RequestContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 获取身份域编码
///
@Slf4j
@Service
public class ClientCodeService {

    /// 获取身份域编码
    public String getClientCode(){
        return RequestContextHolder.getClientCode();
    }
}

