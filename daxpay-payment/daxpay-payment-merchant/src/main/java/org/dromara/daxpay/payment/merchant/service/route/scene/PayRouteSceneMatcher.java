package org.dromara.daxpay.payment.merchant.service.route.scene;

import org.dromara.daxpay.payment.merchant.entity.route.scene.PayRouteSceneConfig;
import org.dromara.daxpay.payment.merchant.service.route.model.RouteHit;
import org.dromara.daxpay.payment.unipay.param.trade.pay.PayParam;
import org.dromara.daxpay.platform.core.code.CommonErrorCode;
import org.dromara.daxpay.platform.core.exception.BizInfoException;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Objects;

/// # 场景模式通道路由匹配器
///
/// 配置粒度为「支付渠道 + 支付方式 → 产品/通道」；下单须同时传 provider 与 method（含 barcode 消歧）。
@UtilityClass
public class PayRouteSceneMatcher {

    /// 场景模式匹配：按 provider + method 精确命中唯一配置行
    public RouteHit match(List<PayRouteSceneConfig> configs, PayParam payParam) {
        if (CollUtil.isEmpty(configs)) {
            return null;
        }
        if (StrUtil.isBlank(payParam.getProvider())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.providerRequiredForScene");
        }
        if (StrUtil.isBlank(payParam.getMethod())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.payment.capability.methodRequiredWithPayProvider");
        }
        List<PayRouteSceneConfig> candidates = configs.stream()
                .filter(config -> Objects.equals(config.getProvider(), payParam.getProvider()))
                .filter(config -> Objects.equals(config.getMethod(), payParam.getMethod()))
                .toList();
        if (candidates.isEmpty()) {
            return null;
        }
        if (candidates.size() > 1) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.duplicateSceneConfig");
        }
        return RouteHit.fromScene(candidates.getFirst());
    }
}
