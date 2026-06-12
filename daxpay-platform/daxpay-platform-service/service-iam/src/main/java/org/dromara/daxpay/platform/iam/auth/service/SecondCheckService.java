package org.dromara.daxpay.platform.iam.auth.service;

import org.dromara.daxpay.platform.iam.param.auth.LoginContentParam;
import org.dromara.daxpay.platform.iam.result.auth.SecondCheckResult;
import org.springframework.stereotype.Service;

/// # 二次校验信息服务
///
@Service
public class SecondCheckService {

    /// 获取当前登录链路需要的二次校验信息
    public SecondCheckResult getSecondCheck(LoginContentParam param) {
        return new SecondCheckResult()
                .setRequired(false)
                .setType("NONE")
                .setMessage("当前登录链路无需二次校验");
    }

}


