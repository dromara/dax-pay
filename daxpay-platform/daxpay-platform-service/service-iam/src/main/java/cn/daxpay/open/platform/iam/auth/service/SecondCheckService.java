package cn.daxpay.open.platform.iam.auth.service;

import cn.daxpay.open.platform.iam.param.auth.LoginContentParam;
import cn.daxpay.open.platform.iam.result.auth.SecondCheckResult;
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


