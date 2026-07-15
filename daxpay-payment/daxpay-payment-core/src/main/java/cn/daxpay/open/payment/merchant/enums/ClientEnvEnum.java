package cn.daxpay.open.payment.merchant.enums;

import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 客户端环境(UA/宿主识别)
///
/// 字典: client_env；
/// 聚合扫码与收银台 H5 共用；
/// 聚合使用微信/支付宝/云闪付/抖音；
/// 收银台 H5 额外支持 browser(普通浏览器), 聚合配置不使用 browser。
/// 非支付渠道(PayProvider/Channel)、非沙箱环境(PayEnv)。
@Getter
@RequiredArgsConstructor
public enum ClientEnvEnum implements I18nSupport {

    /// 普通浏览器环境(仅收银台 H5 配置使用)
    BROWSER("browser"),
    /// 微信环境
    WECHAT_PAY("wechat_pay"),
    /// 支付宝环境
    ALIPAY("alipay"),
    /// 云闪付环境
    UNION_PAY("union_pay"),
    /// 抖音环境
    DOUYIN("douyin"),
    ;

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.client_env";
    }

    public static ClientEnvEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("error.common.clientEnvNotExist", code));
    }

    /// 聚合扫码 L1 AUTO：按客户端环境推导默认支付方式（H5 默认 JSAPI）
    public String defaultMethodCode() {
        return defaultMethodCode(ClientRuntimeEnum.H5);
    }

    /// 按运行形态推导默认支付方式: H5→JSAPI, 小程序→MINI(无独立 mini 时回退 JSAPI)
    public String defaultMethodCode(ClientRuntimeEnum runtime) {
        ClientRuntimeEnum rt = runtime == null ? ClientRuntimeEnum.H5 : runtime;
        PayMethodEnum method;
        if (rt == ClientRuntimeEnum.MINI) {
            method = switch (this) {
                case WECHAT_PAY -> PayMethodEnum.WECHAT_MINI;
                case ALIPAY -> PayMethodEnum.ALIPAY_MINI;
                case UNION_PAY -> PayMethodEnum.UNION_JSAPI;
                case DOUYIN -> PayMethodEnum.DOUYIN_JSAPI;
                default -> null;
            };
        } else {
            method = switch (this) {
                case WECHAT_PAY -> PayMethodEnum.WECHAT_JSAPI;
                case ALIPAY -> PayMethodEnum.ALIPAY_JSAPI;
                case UNION_PAY -> PayMethodEnum.UNION_JSAPI;
                case DOUYIN -> PayMethodEnum.DOUYIN_JSAPI;
                // browser 等非聚合扫码环境不支持
                default -> null;
            };
        }
        if (method == null) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.gateway.clientEnvNotSupport");
        }
        return method.getCode();
    }

    /// METHOD 配置的 method 按 runtime 升级(jsapi→mini), 商户心智保持「配微信/支付宝」即可
    public static String adaptMethodForRuntime(String method, ClientRuntimeEnum runtime) {
        if (StrUtil.isBlank(method) || runtime != ClientRuntimeEnum.MINI) {
            return method;
        }
        if (PayMethodEnum.WECHAT_JSAPI.getCode().equals(method)) {
            return PayMethodEnum.WECHAT_MINI.getCode();
        }
        if (PayMethodEnum.ALIPAY_JSAPI.getCode().equals(method)) {
            return PayMethodEnum.ALIPAY_MINI.getCode();
        }
        return method;
    }
}
