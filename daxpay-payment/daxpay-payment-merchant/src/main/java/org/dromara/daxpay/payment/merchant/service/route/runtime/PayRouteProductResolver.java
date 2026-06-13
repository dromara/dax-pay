package org.dromara.daxpay.payment.merchant.service.route.runtime;

import org.dromara.daxpay.payment.merchant.service.route.support.PayRouteStrategyCapabilitySupport;
import org.dromara.daxpay.platform.core.enums.pay.channel.PayMethodEnum;
import org.dromara.daxpay.platform.core.enums.pay.channel.ProductEnum;
import org.dromara.daxpay.platform.core.exception.BizInfoException;
import org.dromara.daxpay.platform.core.code.CommonErrorCode;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;

/// # 通道路由产品解析器
///
/// 开源版：所有产品默认可用，直接从产品枚举中按通道+支付方式解析产品编码。
@Service
@RequiredArgsConstructor
public class PayRouteProductResolver {

    private final PayRouteStrategyCapabilitySupport payRouteStrategyCapabilitySupport;

    /// 根据通道及支付方式解析产品编码（开源版：所有产品默认可用）
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