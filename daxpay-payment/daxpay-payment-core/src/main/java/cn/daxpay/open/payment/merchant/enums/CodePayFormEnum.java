package cn.daxpay.open.payment.merchant.enums;

import cn.daxpay.open.payment.device.enums.QrCodeProgramTypeEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 码牌支付策略形态
///
/// 与码牌落地 [QrCodeProgramTypeEnum] 对应: h5 码读 h5 策略行, mini_app 码读 mini 策略行。
/// 字典: code_pay_form
@Getter
@RequiredArgsConstructor
public enum CodePayFormEnum implements I18nSupport {

    /// H5 落地支付
    H5("h5"),
    /// 小程序落地支付
    MINI("mini"),
    ;

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.code_pay_form";
    }

    public static CodePayFormEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("error.common.codePayFormNotExist", code));
    }

    /// 由码牌 programType 映射策略形态; 空或未知按 H5
    public static CodePayFormEnum fromProgramType(String programType) {
        if (StrUtil.isNotBlank(programType)
                && QrCodeProgramTypeEnum.MINI_APP.getCode().equals(programType)) {
            return MINI;
        }
        return H5;
    }

    /// AUTO 模式默认支付方式(所见即所得; 微信 H5→jsapi/小程序→mini, 支付宝统一 jsapi)
    public String defaultMethodCode(ClientEnvEnum clientEnv) {
        if (this == MINI) {
            return switch (clientEnv) {
                case WECHAT -> PayMethodEnum.WECHAT_MINI.getCode();
                // 支付宝官方 JSAPI 即小程序场景
                case ALIPAY -> PayMethodEnum.ALIPAY_JSAPI.getCode();
                case UNION_PAY -> PayMethodEnum.UNION_JSAPI.getCode();
                case DOUYIN -> PayMethodEnum.DOUYIN_JSAPI.getCode();
                default -> throw new DataNotExistException("error.common.clientEnvNotExist", clientEnv.getCode());
            };
        }
        return switch (clientEnv) {
            case WECHAT -> PayMethodEnum.WECHAT_JSAPI.getCode();
            case ALIPAY -> PayMethodEnum.ALIPAY_JSAPI.getCode();
            case UNION_PAY -> PayMethodEnum.UNION_JSAPI.getCode();
            case DOUYIN -> PayMethodEnum.DOUYIN_JSAPI.getCode();
            default -> throw new DataNotExistException("error.common.clientEnvNotExist", clientEnv.getCode());
        };
    }
}
