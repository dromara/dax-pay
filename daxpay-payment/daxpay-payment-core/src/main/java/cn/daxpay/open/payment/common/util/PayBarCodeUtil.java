package cn.daxpay.open.payment.common.util;

import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;

import java.util.Set;
import java.util.regex.Pattern;

/// # 付款码识别工具
///
/// 按付款码前缀识别底层钱包并映射为平台支付方式, 供普通支付在路由前 method 回填使用。
/// 规则对齐商业版 `PayUtil.getBarCodeType`（微信 10–15 / 支付宝 25–30 / 银联 62）。
/// 前缀命中后追加完整格式校验(纯数字 + 精确长度), 不合规在识别层快速失败, 避免无效码进入通道路由与下单链路。
@UtilityClass
public class PayBarCodeUtil {

    private static final Set<String> WECHAT_PREFIXES = Set.of("10", "11", "12", "13", "14", "15");
    private static final Set<String> ALIPAY_PREFIXES = Set.of("25", "26", "27", "28", "29", "30");
    private static final String UNION_PREFIX = "62";

    /// 微信付款码长度: 18 位
    private static final int WECHAT_CODE_LENGTH = 18;

    /// 支付宝付款码长度区间: 16~24 位(前缀 25–30 已在识别阶段限定)
    private static final int ALIPAY_CODE_MIN_LENGTH = 16;
    private static final int ALIPAY_CODE_MAX_LENGTH = 24;

    /// 银联付款码长度: 19 位(前缀 62 已在识别阶段限定)
    private static final int UNION_CODE_LENGTH = 19;

    /// 纯数字校验(显式 ASCII 字符集, 与付款码实际编码一致)
    private static final Pattern DIGITS_PATTERN = Pattern.compile("[0-9]+");

    /// 根据付款码解析平台支付方式编码
    ///
    /// @param authCode 付款码
    /// @return `wechat_barcode` / `alipay_barcode` / `union_barcode`
    public String resolveMethodCode(String authCode) {
        return resolveMethod(authCode).getCode();
    }

    /// 根据付款码解析平台支付方式枚举
    ///
    /// 两步识别: 先按前两位白名单判定渠道, 再按渠道规则校验完整格式(纯数字 + 精确长度);
    /// 格式不合规同样抛参数校验异常(错误码不变), 仅文案按渠道分化。
    public PayMethodEnum resolveMethod(String authCode) {
        if (StrUtil.isBlank(authCode) || authCode.length() < 2) {
            // 支付: 付款码无效
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.barcode.invalid");
        }
        String prefix = authCode.substring(0, 2);
        if (WECHAT_PREFIXES.contains(prefix)) {
            // 支付: 微信付款码应为18位数字
            requireDigitsLength(authCode, WECHAT_CODE_LENGTH, WECHAT_CODE_LENGTH, "pay.error.barcode.wechatFormatInvalid");
            return PayMethodEnum.WECHAT_BARCODE;
        }
        if (ALIPAY_PREFIXES.contains(prefix)) {
            // 支付: 支付宝付款码应为25~30开头的16~24位数字
            requireDigitsLength(authCode, ALIPAY_CODE_MIN_LENGTH, ALIPAY_CODE_MAX_LENGTH,
                    "pay.error.barcode.alipayFormatInvalid");
            return PayMethodEnum.ALIPAY_BARCODE;
        }
        if (UNION_PREFIX.equals(prefix)) {
            // 支付: 银联付款码应为62开头的19位数字
            requireDigitsLength(authCode, UNION_CODE_LENGTH, UNION_CODE_LENGTH, "pay.error.barcode.unionFormatInvalid");
            return PayMethodEnum.UNION_BARCODE;
        }
        // 支付: 不支持的付款码类型
        throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.barcode.unsupportedType");
    }

    /// 若已传分钱包付款码 method, 校验 authCode 与 method 一致
    ///
    /// 条码分支先走 [PayBarCodeUtil#resolveMethod] 的完整格式校验(格式不合规先报格式错误),
    /// 再比对渠道是否一致; 非条码 method 不参与付款码校验, 直接放行。
    public void validateMethodMatchesAuthCode(String methodCode, String authCode) {
        if (StrUtil.isBlank(methodCode) || StrUtil.isBlank(authCode)) {
            return;
        }
        if (!isBarcodeMethod(methodCode)) {
            return;
        }
        PayMethodEnum resolved = resolveMethod(authCode);
        if (!resolved.getCode().equals(methodCode)) {
            // 支付: 付款码与支付方式不匹配
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.barcode.methodMismatch");
        }
    }

    /// 是否为分钱包付款码支付方式
    public boolean isBarcodeMethod(String methodCode) {
        return PayMethodEnum.WECHAT_BARCODE.getCode().equals(methodCode)
                || PayMethodEnum.ALIPAY_BARCODE.getCode().equals(methodCode)
                || PayMethodEnum.UNION_BARCODE.getCode().equals(methodCode);
    }

    /// 校验付款码完整格式: 纯数字且长度落在指定区间, 不合规抛「参数校验错误」
    ///
    /// @param authCode   付款码
    /// @param minLength  最短长度(含)
    /// @param maxLength  最长长度(含)
    /// @param messageKey 渠道对应的格式错误文案 i18n key
    private void requireDigitsLength(String authCode, int minLength, int maxLength, String messageKey) {
        int length = authCode.length();
        boolean valid = length >= minLength && length <= maxLength && DIGITS_PATTERN.matcher(authCode).matches();
        if (!valid) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, messageKey);
        }
    }
}
