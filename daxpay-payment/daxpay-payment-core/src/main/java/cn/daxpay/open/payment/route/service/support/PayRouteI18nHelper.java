package cn.daxpay.open.payment.route.service.support;

import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Objects;

/// # 通道路由异常文案中的枚举展示名
///
/// 抛错前将 product / payMethod / provider 等 code 转为当前语言的枚举文案，未命中时回退 code。
@UtilityClass
public class PayRouteI18nHelper {

    /// 支付产品展示名（如 ums_mini → 银联商务(小程序)）
    public String product(String productCode) {
        if (StrUtil.isBlank(productCode)) {
            return productCode;
        }
        // 路由配置中的 product 可能为非标准编码（自定义/历史通道），未命中时回退原始 code，不抛异常
        ProductEnum productEnum = Arrays.stream(ProductEnum.values())
                .filter(e -> e.getCode().equals(productCode))
                .findFirst()
                .orElse(null);
        return label(productEnum, productCode);
    }

    /// 支付方式展示名
    public String payMethod(String methodCode) {
        if (StrUtil.isBlank(methodCode)) {
            return methodCode;
        }
        return Arrays.stream(PayMethodEnum.values())
                .filter(method -> Objects.equals(method.getCode(), methodCode))
                .findFirst()
                .map(I18nUtil::getEnumName)
                .orElse(methodCode);
    }

    /// 支付渠道展示名
    public String provider(String providerCode) {
        if (StrUtil.isBlank(providerCode)) {
            return providerCode;
        }
        PayProviderEnum provider = PayProviderEnum.findByCode(providerCode);
        return label(provider, providerCode);
    }

    /// 取枚举国际化名称，未配置则回退原始 code
    private String label(I18nSupport i18nSupport, String fallbackCode) {
        if (i18nSupport == null) {
            return fallbackCode;
        }
        String name = I18nUtil.getEnumName(i18nSupport);
        return StrUtil.isNotBlank(name) ? name : fallbackCode;
    }
}
