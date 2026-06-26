package cn.daxpay.open.payment.merchant.service.route.runtime;

import cn.daxpay.open.payment.channel.dao.mch.ChannelMerchantManager;
import cn.daxpay.open.payment.channel.entity.mch.ChannelMerchant;
import cn.daxpay.open.payment.merchant.service.route.support.PayRouteStrategyCapabilitySupport;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;

/// # 通道路由产品解析器
///
/// 通道商户号(channelMchNo)与支付产品一一绑定，是路由重构后的唯一定位锚点；
/// 通道(channel)/支付方式(method)→产品的旧解析逻辑暂时保留以兼容基础模式回退场景。
@Service
@RequiredArgsConstructor
public class PayRouteProductResolver {

    private final PayRouteStrategyCapabilitySupport payRouteStrategyCapabilitySupport;
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

    /// 根据通道商户号解析所属通道编码
    public String channelOfChannelMchNo(String channelMchNo) {
        return channelOfProduct(productOfChannelMchNo(channelMchNo));
    }

    /// 根据通道及支付方式解析产品编码
    public String resolve(String mchNo, String channel, String method) {
        return Arrays.stream(ProductEnum.values())
                .filter(pe -> Objects.equals(pe.getChannel(), channel))
                .map(ProductEnum::getCode)
                .filter(product -> payRouteStrategyCapabilitySupport.routeProductSupportsMethod(
                        product, PayMethodEnum.findByCode(method)))
                .findFirst()
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.route.error.productNotResolved", channel, method));
    }

    /// 填充并校验产品字段
    public String resolveAndFill(String mchNo, String channel, String method, String product) {
        String resolved = resolve(mchNo, channel, method);
        if (StrUtil.isNotBlank(product) && !Objects.equals(product, resolved)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.productMismatch", product, resolved);
        }
        return resolved;
    }

    /// 根据产品编码填充通道
    public String channelOfProduct(String product) {
        ProductEnum productEnum = ProductEnum.findByCode(product);
        if (productEnum == null) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.productInvalid", product);
        }
        return productEnum.getChannel();
    }
}
