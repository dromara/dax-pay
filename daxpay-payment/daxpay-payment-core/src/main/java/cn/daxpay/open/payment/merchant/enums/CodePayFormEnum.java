package cn.daxpay.open.payment.merchant.enums;

import cn.daxpay.open.payment.device.enums.QrCodeProgramTypeEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 网关支付配置形态(码牌/聚合共用)
///
/// 字典: code_pay_form
/// - 码牌: 由 [QrCodeProgramTypeEnum] 映射(h5 码读 h5 策略行, mini_app 码读 mini 策略行)
/// - 聚合: 由运行形态 [ClientRuntimeEnum] 映射(runtime=h5→H5, runtime=mini→MINI)
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

    /// 由聚合运行形态映射策略形态; 空或 H5 按 H5
    public static CodePayFormEnum fromRuntime(ClientRuntimeEnum runtime) {
        return runtime == ClientRuntimeEnum.MINI ? MINI : H5;
    }

    /// AUTO 模式默认支付方式(所见即所得)
    /// 微信: H5→jsapi / 小程序→mini
    /// 支付宝: H5→扫码(alipay_qr, 免 OAuth) / 小程序→jsapi(无独立 mini)
    /// 云闪付、抖音: 统一 jsapi
    public String defaultMethodCode(ClientEnvEnum clientEnv) {
        if (this == MINI) {
            return switch (clientEnv) {
                case WECHAT -> PayMethodEnum.WECHAT_MINI.getCode();
                // 支付宝官方 JSAPI 即小程序场景
                case ALIPAY -> PayMethodEnum.ALIPAY_JSAPI.getCode();
                case UNION_PAY -> PayMethodEnum.UNION_JSAPI.getCode();
                case DOUYIN -> PayMethodEnum.DOUYIN_JSAPI.getCode();
                // 码牌: 不支持的客户端环境
                default -> throw new DataNotExistException("error.common.clientEnvNotExist", clientEnv.getCode());
            };
        }
        return switch (clientEnv) {
            case WECHAT -> PayMethodEnum.WECHAT_JSAPI.getCode();
            // 支付宝 H5 默认扫码(alipay_qr): 免 OAuth, 预下单返回支付链接
            case ALIPAY -> PayMethodEnum.ALIPAY_QR.getCode();
            case UNION_PAY -> PayMethodEnum.UNION_JSAPI.getCode();
            case DOUYIN -> PayMethodEnum.DOUYIN_JSAPI.getCode();
            // 码牌: 不支持的客户端环境
            default -> throw new DataNotExistException("error.common.clientEnvNotExist", clientEnv.getCode());
        };
    }
}
