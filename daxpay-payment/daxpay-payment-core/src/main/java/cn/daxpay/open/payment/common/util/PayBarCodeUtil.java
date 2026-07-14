package cn.daxpay.open.payment.common.util;

import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;

import java.util.Set;

/// # 付款码识别工具
///
/// 按付款码前缀识别底层钱包并映射为平台支付方式, 供普通支付在路由前 method 回填使用。
/// 规则对齐商业版 `PayUtil.getBarCodeType`（微信 10–15 / 支付宝 25–30 / 银联 62）。
@UtilityClass
public class PayBarCodeUtil {

    private static final Set<String> WECHAT_PREFIXES = Set.of("10", "11", "12", "13", "14", "15");
    private static final Set<String> ALIPAY_PREFIXES = Set.of("25", "26", "27", "28", "29", "30");
    private static final String UNION_PREFIX = "62";

    /// 根据付款码解析平台支付方式编码
    ///
    /// @param authCode 付款码
    /// @return `wechat_barcode` / `alipay_barcode` / `union_pay_barcode`
    public String resolveMethodCode(String authCode) {
        return resolveMethod(authCode).getCode();
    }

    /// 根据付款码解析平台支付方式枚举
    public PayMethodEnum resolveMethod(String authCode) {
        if (StrUtil.isBlank(authCode) || authCode.length() < 2) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.barcode.invalid");
        }
        String prefix = authCode.substring(0, 2);
        if (WECHAT_PREFIXES.contains(prefix)) {
            return PayMethodEnum.WECHAT_BARCODE;
        }
        if (ALIPAY_PREFIXES.contains(prefix)) {
            return PayMethodEnum.ALIPAY_BARCODE;
        }
        if (UNION_PREFIX.equals(prefix)) {
            return PayMethodEnum.UNION_PAY_BARCODE;
        }
        throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.barcode.unsupportedType");
    }

    /// 若已传分钱包付款码 method, 校验 authCode 前缀与 method 一致
    public void validateMethodMatchesAuthCode(String methodCode, String authCode) {
        if (StrUtil.isBlank(methodCode) || StrUtil.isBlank(authCode)) {
            return;
        }
        if (!isBarcodeMethod(methodCode)) {
            return;
        }
        PayMethodEnum resolved = resolveMethod(authCode);
        if (!resolved.getCode().equals(methodCode)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.barcode.methodMismatch");
        }
    }

    /// 是否为分钱包付款码支付方式
    public boolean isBarcodeMethod(String methodCode) {
        return PayMethodEnum.WECHAT_BARCODE.getCode().equals(methodCode)
                || PayMethodEnum.ALIPAY_BARCODE.getCode().equals(methodCode)
                || PayMethodEnum.UNION_PAY_BARCODE.getCode().equals(methodCode);
    }
}
