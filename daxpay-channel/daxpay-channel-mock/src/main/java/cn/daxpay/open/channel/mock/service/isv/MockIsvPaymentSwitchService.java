package cn.daxpay.open.channel.mock.service.isv;

import cn.daxpay.open.channel.mock.dao.isv.MockIsvChannelMerchantManager;
import cn.daxpay.open.channel.mock.dao.isv.MockIsvInfoManager;
import cn.daxpay.open.channel.mock.entity.isv.MockIsvChannelMerchant;
import cn.daxpay.open.channel.mock.entity.isv.MockIsvInfo;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # Mock 服务商支付开关校验
///
/// 仅在新支付下单链路校验模拟服务商支付状态(payEnable=false 拒新支付),
/// 不影响退款、关单、查询、同步和回调(机制对齐支付宝服务商支付开关校验)。
@Service
@RequiredArgsConstructor
public class MockIsvPaymentSwitchService {

    private final MockIsvChannelMerchantManager mockIsvChannelMerchantManager;
    private final MockIsvInfoManager mockIsvInfoManager;

    /// 校验通道商户归属的模拟服务商是否允许发起新支付
    ///
    /// @param channelMchNo 通道商户号(服务商归属绑定定位主键)
    public void assertPaymentEnabled(String channelMchNo) {
        MockIsvChannelMerchant isvMerchant = mockIsvChannelMerchantManager.findByChannelMchNo(channelMchNo)
                // mock: 模拟服务商通道商户绑定不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.mock.isvBindingNotExist", channelMchNo));
        MockIsvInfo isv = mockIsvInfoManager.findById(isvMerchant.getIsvId())
                // mock: 模拟服务商不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.mock.isvNotExist", isvMerchant.getIsvId()));
        if (!Boolean.TRUE.equals(isv.getPayEnable())) {
            // 模拟服务商[{0}]已关闭支付, 无法发起新支付
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.mock.isvPayDisabled", isv.getIsvName());
        }
    }
}
