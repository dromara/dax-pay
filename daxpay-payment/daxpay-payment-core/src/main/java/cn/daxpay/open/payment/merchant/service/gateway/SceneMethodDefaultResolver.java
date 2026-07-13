package cn.daxpay.open.payment.merchant.service.gateway;

import cn.daxpay.open.payment.merchant.enums.CashierSceneEnum;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import lombok.experimental.UtilityClass;

import java.util.Map;

/// # 聚合扫码场景默认支付方式推导器(L1 自动模式)
///
/// L1 AUTO 模式下, 系统按扫码环境自动推导支付方式, 无需商户手动配置。
/// 推导规则: 原生 APP 环境一律使用 JSAPI 支付方式。
@UtilityClass
public class SceneMethodDefaultResolver {

    /// 场景 → 默认支付方式映射
    private static final Map<CashierSceneEnum, PayMethodEnum> DEFAULT_METHODS = Map.of(
            CashierSceneEnum.WECHAT_PAY, PayMethodEnum.WECHAT_JSAPI,
            CashierSceneEnum.ALIPAY, PayMethodEnum.ALIPAY_JSAPI,
            CashierSceneEnum.UNION_PAY, PayMethodEnum.UNION_JSAPI,
            CashierSceneEnum.DOUYIN, PayMethodEnum.DOUYIN_JSAPI
    );

    /// 按场景推导默认支付方式编码
    public static String resolve(CashierSceneEnum scene) {
        PayMethodEnum method = DEFAULT_METHODS.get(scene);
        if (method == null) {
            // 不支持的收银场景
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.gateway.sceneNotSupport");
        }
        return method.getCode();
    }
}
