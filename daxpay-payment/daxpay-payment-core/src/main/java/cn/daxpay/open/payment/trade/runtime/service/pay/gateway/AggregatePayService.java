package cn.daxpay.open.payment.trade.runtime.service.pay.gateway;

import cn.daxpay.open.payment.common.util.PayMethodOpenIdSupport;
import cn.daxpay.open.payment.merchant.dao.gateway.GatewayAggregateConfigManager;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayAggregateConfig;
import cn.daxpay.open.payment.merchant.enums.ClientEnvEnum;
import cn.daxpay.open.payment.merchant.enums.ClientRuntimeEnum;
import cn.daxpay.open.payment.merchant.service.gateway.ClientEnvPayResolveService;
import cn.daxpay.open.payment.strategy.risk.PayRiskChecker;
import cn.daxpay.open.payment.trade.enums.GatewayOrderStatusEnum;
import cn.daxpay.open.payment.trade.enums.GatewayPayTypeEnum;
import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.unipay.param.gateway.AggregateQrPayParam;
import cn.daxpay.open.payment.unipay.result.gateway.AggregatePayMetaResult;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayResult;
import cn.daxpay.open.platform.common.spring.util.WebServletUtil;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.system.enums.PayRiskOpenIdLevelEnum;
import cn.daxpay.open.platform.system.service.config.security.PlatformSecurityConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 聚合扫码支付服务
///
/// 按应用聚合配置的深度(level)解析支付方式, 委托 [ClientEnvPayResolveService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class AggregatePayService {

    private final GatewayPayAssistService gatewayPayAssistService;
    private final ClientEnvPayResolveService clientEnvPayResolveService;
    private final GatewayPayHandleService gatewayPayHandleService;
    private final GatewayAggregateConfigManager aggregateConfigManager;
    /// 风控检查器（可选 SPI：用于判断是否存在 openId 黑名单, 决定是否触发强制 OAuth）
    private final ObjectProvider<PayRiskChecker> payRiskCheckerProvider;
    /// 平台安全配置（读取用户标识拦截级别, 决定 NORMAL 模式下不触发强制 OAuth）
    private final PlatformSecurityConfigService platformSecurityConfigService;

    /// H5 聚合元数据: autoLaunch / needOpenId(不下发敏感路由字段)
    public AggregatePayMetaResult getMeta(String orderNo, String clientEnvCode, String runtimeCode) {
        GatewayPayOrder order = gatewayPayAssistService.getOrderAndCheck(orderNo);
        if (!Objects.equals(order.getGatewayType(), GatewayPayTypeEnum.AGGREGATE.getCode())) {
            // 聚合: 网关订单类型不匹配
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.gateway.typeMismatch");
        }
        ClientEnvEnum clientEnv = ClientEnvEnum.findByCode(clientEnvCode);
        ClientRuntimeEnum runtime = ClientRuntimeEnum.ofOrDefault(runtimeCode);
        var resolved = clientEnvPayResolveService.resolveRequired(order.getAppId(), clientEnv, runtime);

        // 订单已发起支付(支付中)时, 支付方式已锁定到容器: 当前环境解析出的 method 与锁定值不一致视为换端, 提前拒绝
        // 与 GatewayPayHandleService#handle 的 method 比较语义一致(聚合 product 传 null, 实际只比 method)
        if (Objects.equals(order.getStatus(), GatewayOrderStatusEnum.PAYING.getCode())
                && StrUtil.isNotBlank(order.getMethod())
                && !Objects.equals(order.getMethod(), resolved.method())) {
            // 聚合: 订单已锁定支付方式请勿切换
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.channelLocked");
        }

        GatewayAggregateConfig config = aggregateConfigManager.findByAppId(order.getAppId())
                // 聚合: 应用未配置聚合扫码支付
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.gateway.aggregateConfigMissing"));

        return new AggregatePayMetaResult()
                .setAutoLaunch(Boolean.TRUE.equals(config.getAutoLaunch()))
                // openId 触发判定: 业务必需(JSAPI/MINI) 或 存在 openId 黑名单且当前环境可 OAuth
                .setNeedOpenId(this.resolveNeedOpenId(resolved.method(), clientEnv));
    }

    /// 聚合扫码发起支付
    public NormalPayResult aggregateQrPay(AggregateQrPayParam param) {
        GatewayPayOrder order = gatewayPayAssistService.getOrderAndCheck(param.getOrderNo());
        if (!Objects.equals(order.getGatewayType(), GatewayPayTypeEnum.AGGREGATE.getCode())) {
            // 聚合: 网关订单类型不匹配
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.gateway.typeMismatch");
        }
        ClientEnvEnum clientEnv = ClientEnvEnum.findByCode(param.getClientEnv());
        // 一期 H5; 小程序接入口时传 runtime=mini
        ClientRuntimeEnum runtime = ClientRuntimeEnum.ofOrDefault(param.getRuntime());
        var resolved = clientEnvPayResolveService.resolveRequired(order.getAppId(), clientEnv, runtime);

        String clientIp = StrUtil.blankToDefault(param.getClientIp(), WebServletUtil.getClientIp());
        // product 传 null: AUTO/METHOD 由路由解析, DIRECT 由 channelMchNo 派生
        return gatewayPayHandleService.handle(order, null, resolved.method(),
                resolved.channelMchNo(), resolved.capability(),
                param.getOpenId(), clientEnv.getCode(), param.getDevice(), clientIp);
    }

    /// openId 触发判定
    ///
    /// 1. JSAPI/MINI 类方式: 业务必需, 永远 true（与历史行为一致）
    /// 2. 主扫/H5 等免用户标识方式: 仅当用户标识拦截级别为 ENHANCED,
    ///    且存在用户标识黑名单, 且当前 clientEnv 可 OAuth 时 true,
    ///    实现用户标识黑名单在聚合网关内的全局拦截
    private boolean resolveNeedOpenId(String method, ClientEnvEnum clientEnv) {
        if (PayMethodOpenIdSupport.needsOpenId(method)) {
            return true;
        }
        PayRiskChecker checker = payRiskCheckerProvider.getIfAvailable();
        if (checker == null || !isEnhancedOpenIdLevel() || !checker.hasOpenIdBlacklist()) {
            return false;
        }
        return PayMethodOpenIdSupport.canAcquireOpenId(method, clientEnv);
    }

    /// 用户标识拦截级别是否为增强模式（NORMAL 时跳过强制 OAuth, 保留用户体验）
    private boolean isEnhancedOpenIdLevel() {
        String level = platformSecurityConfigService.getPaySecurityConfig().getRiskOpenIdLevel();
        return PayRiskOpenIdLevelEnum.ENHANCED.getCode().equals(level);
    }
}
