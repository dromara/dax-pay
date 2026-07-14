package cn.daxpay.open.payment.merchant.service.gateway;

import cn.daxpay.open.payment.merchant.enums.ClientEnvEnum;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import lombok.experimental.UtilityClass;

import java.util.Map;

/// # 聚合扫码客户端环境默认支付方式推导器(L1 自动模式)
///
/// L1 AUTO 模式下, 系统按扫码环境自动推导支付方式, 无需商户手动配置。
/// 推导规则: 原生 APP 环境一律使用 JSAPI 支付方式。
@UtilityClass
public class ClientEnvMethodDefaultResolver {

    /// 客户端环境 → 默认支付方式映射
    private static final Map<ClientEnvEnum, PayMethodEnum> DEFAULT_METHODS = Map.of(
            ClientEnvEnum.WECHAT_PAY, PayMethodEnum.WECHAT_JSAPI,
            ClientEnvEnum.ALIPAY, PayMethodEnum.ALIPAY_JSAPI,
            ClientEnvEnum.UNION_PAY, PayMethodEnum.UNION_JSAPI,
            ClientEnvEnum.DOUYIN, PayMethodEnum.DOUYIN_JSAPI
    );

    /// 按客户端环境推导默认支付方式编码
    public static String resolve(ClientEnvEnum clientEnv) {
        PayMethodEnum method = DEFAULT_METHODS.get(clientEnv);
        if (method == null) {
            // 不支持的客户端环境
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.gateway.clientEnvNotSupport");
        }
        return method.getCode();
    }
}
