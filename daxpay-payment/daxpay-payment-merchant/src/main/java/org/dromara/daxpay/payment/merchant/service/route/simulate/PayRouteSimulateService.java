package org.dromara.daxpay.payment.merchant.service.route.simulate;

import org.dromara.daxpay.payment.merchant.dao.appinfo.MchAppInfoManager;
import org.dromara.daxpay.payment.merchant.dao.route.strategy.PayRouteStrategyManager;
import org.dromara.daxpay.payment.merchant.entity.route.strategy.PayRouteStrategy;
import org.dromara.daxpay.payment.merchant.param.route.resolve.PayRouteSimulateParam;
import org.dromara.daxpay.payment.merchant.result.route.resolve.PayRouteResolveResult;
import org.dromara.daxpay.payment.merchant.service.route.runtime.PayRouteService;
import org.dromara.daxpay.payment.unipay.param.trade.pay.PayParam;
import org.dromara.daxpay.platform.core.enums.pay.route.PayRouteModeEnum;
import org.dromara.daxpay.platform.core.exception.BizInfoException;
import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.platform.core.code.CommonErrorCode;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 通道路由试算
///
/// 管理端路由模拟解析入口，不写真实订单；仅支持基础/场景模式，精细模式由 PayRouteService 统一拒绝。
///
@Service
@RequiredArgsConstructor
public class PayRouteSimulateService {

    private final PayRouteStrategyManager strategyManager;
    private final MchAppInfoManager mchAppInfoManager;
    private final PayRouteService payRouteService;

    /// 管理端模拟路由（可指定试算模式，委托 PayRouteService.simulate）
    public PayRouteResolveResult simulate(PayRouteSimulateParam param) {
        String mchNo = mchAppInfoManager.requireMchNoByAppIdNotTenant(param.getAppId());
        if (!Objects.equals(mchNo, param.getMchNo())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.assist.mchNoAppNoMatch");
        }
        PayParam payParam = new PayParam();
        payParam.setMchNo(mchNo);
        payParam.setAppId(param.getAppId());
        payParam.setProvider(param.getProvider());
        payParam.setMethod(param.getMethod());
        payParam.setAmount(param.getAmount());
        payParam.setBizOrderNo("SIMULATE");
        payParam.setTitle("simulate");
        String simulateMode = normalizeSimulateMode(param.getMode(), requireStrategy(param.getAppId()).getMode());
        if (!Objects.equals(simulateMode, PayRouteModeEnum.ADVANCED.getCode()) && StrUtil.isBlank(param.getProvider())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.providerRequired");
        }
        // 场景模式须指定目录内方式，才能命中 pay_route_scene_config 唯一行
        if (Objects.equals(simulateMode, PayRouteModeEnum.SCENE.getCode()) && StrUtil.isBlank(param.getMethod())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.payment.capability.methodRequiredWithPayProvider");
        }
        var hit = payRouteService.simulate(payParam, simulateMode);
        return new PayRouteResolveResult()
                .setChannel(payParam.getChannel())
                .setMethod(payParam.getMethod())
                .setProduct(payParam.getProduct())
                .setHitRuleId(hit.hitRuleId() == null ? null : String.valueOf(hit.hitRuleId()))
                .setHitConfigId(hit.hitConfigId() == null ? null : String.valueOf(hit.hitConfigId()))
                .setMode(simulateMode);
    }

    /// 规范化试算模式：请求未传则用策略生效模式；历史值 simple 映射为 scene
    private String normalizeSimulateMode(String requestMode, String effectiveMode) {
        String mode = StrUtil.isNotBlank(requestMode) ? requestMode : effectiveMode;
        if (Objects.equals(mode, "simple")) {
            return PayRouteModeEnum.SCENE.getCode();
        }
        return mode;
    }

    /// 按应用号加载路由策略，不存在则抛业务异常
    private PayRouteStrategy requireStrategy(String appId) {
        return strategyManager.findByAppId(appId)
                .orElseThrow(() -> new DataNotExistException("pay.route.error.routeStrategyNotExist"));
    }
}
