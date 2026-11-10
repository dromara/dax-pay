package cn.daxpay.open.channel.mock.service.isv;

import cn.daxpay.open.channel.mock.entity.isv.MockIsvInfo;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # Mock 服务商支付开关校验
///
/// 多服务商模式取消后全局仅一个模拟服务商, 直接读取单例 payEnable(payEnable=false 拒新支付),
/// 不影响退款、关单、查询、同步和回调(机制对齐支付宝服务商支付开关校验)。
@Service
@RequiredArgsConstructor
public class MockIsvPaymentSwitchService {

    private final MockIsvInfoService mockIsvInfoService;

    /// 校验全局唯一模拟服务商是否允许发起新支付
    public void assertPaymentEnabled() {
        MockIsvInfo isv = mockIsvInfoService.getSingleton();
        if (!Boolean.TRUE.equals(isv.getPayEnable())) {
            // 模拟服务商[{0}]已关闭支付, 无法发起新支付
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.mock.isvPayDisabled", isv.getIsvName());
        }
    }
}
