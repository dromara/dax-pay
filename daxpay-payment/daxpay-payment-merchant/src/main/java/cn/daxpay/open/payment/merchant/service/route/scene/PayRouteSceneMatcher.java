package cn.daxpay.open.payment.merchant.service.route.scene;

import cn.daxpay.open.payment.merchant.entity.route.scene.PayRouteSceneConfig;
import cn.daxpay.open.payment.merchant.service.route.model.RouteHit;
import cn.daxpay.open.payment.unipay.param.trade.pay.PayParam;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Objects;

/// # 场景模式通道路由匹配器
///
/// 配置粒度为「支付方式 → (通道商户, 支付能力)」，每个支付方式唯一一行；
/// 下单须传 method，按 method 精确命中，命中行的 channelMchNo/capability 即路由结果。
@UtilityClass
public class PayRouteSceneMatcher {

    /// 场景模式匹配：按 method 精确命中唯一配置行
    public RouteHit match(List<PayRouteSceneConfig> configs, PayParam payParam) {
        if (CollUtil.isEmpty(configs)) {
            return null;
        }
        if (StrUtil.isBlank(payParam.getMethod())) {
            // 场景模式下须选择支付方式
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.sceneMethodRequired");
        }
        String method = payParam.getMethod();
        List<PayRouteSceneConfig> candidates = configs.stream()
                .filter(config -> Objects.equals(config.getMethod(), method))
                .toList();
        if (candidates.isEmpty()) {
            return null;
        }
        if (candidates.size() > 1) {
            // 场景模式下同一支付方式存在多条配置
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.duplicateSceneMethod",
                    method);
        }
        return RouteHit.fromScene(candidates.getFirst().getChannelMchNo(), candidates.getFirst().getCapability());
    }
}
