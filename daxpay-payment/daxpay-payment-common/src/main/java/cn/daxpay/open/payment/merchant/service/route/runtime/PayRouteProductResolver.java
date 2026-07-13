package cn.daxpay.open.payment.merchant.service.route.runtime;

import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 通道路由产品解析器
///
/// 通道商户号(channelMchNo)与支付产品一一绑定，是路由的唯一定位锚点。
@Service
@RequiredArgsConstructor
public class PayRouteProductResolver {

    private final ChannelMerchantManager channelMerchantManager;

    /// 根据通道商户号解析支付产品编码(channelMchNo 唯一绑定 product)
    public String productOfChannelMchNo(String channelMchNo) {
        if (StrUtil.isBlank(channelMchNo)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.channelMchNoRequired");
        }
        return channelMerchantManager.lambdaQuery()
                .eq(ChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt()
                .map(ChannelMerchant::getProduct)
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.route.error.channelMchNotExist", channelMchNo));
    }
}
