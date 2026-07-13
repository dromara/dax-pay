package cn.daxpay.open.payment.unipay.gateway.service;

import cn.daxpay.open.payment.merchant.enums.AggregateConfigLevelEnum;
import cn.daxpay.open.payment.merchant.enums.CashierSceneEnum;
import cn.daxpay.open.payment.trade.enums.GatewayPayTypeEnum;
import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.merchant.dao.gateway.GatewayAggregateSceneManager;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayAggregateConfig;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayAggregateScene;
import cn.daxpay.open.payment.merchant.service.gateway.GatewayAggregateConfigService;
import cn.daxpay.open.payment.merchant.service.gateway.SceneMethodDefaultResolver;
import cn.daxpay.open.payment.unipay.gateway.param.AggregateQrPayParam;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayResult;
import cn.daxpay.open.platform.common.spring.util.WebServletUtil;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 网关聚合扫码支付服务
///
/// 按应用聚合配置的深度(level)解析支付方式:
/// - AUTO: 系统按扫码环境推导 method, 走路由基础/场景模式
/// - METHOD: 每场景配置的 method, 走路由场景模式
/// - DIRECT: 每场景配置的 channelMchNo+capability, 走路由直定模式
@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayAggregatePayService {

    private final GatewayPayAssistService gatewayPayAssistService;
    private final GatewayAggregateConfigService gatewayAggregateConfigService;
    private final GatewayAggregateSceneManager gatewayAggregateSceneManager;
    private final GatewayPayHandleService gatewayPayHandleService;

    /// 聚合扫码发起支付
    public NormalPayResult aggregateQrPay(AggregateQrPayParam param) {
        GatewayPayOrder order = gatewayPayAssistService.getOrderAndCheck(param.getOrderNo());
        if (!Objects.equals(order.getGatewayType(), GatewayPayTypeEnum.AGGREGATE.getCode())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.gateway.typeMismatch");
        }
        CashierSceneEnum scene = CashierSceneEnum.findByCode(param.getScene());
        GatewayAggregateConfig config = gatewayAggregateConfigService.getRequiredByAppId(order.getAppId());
        AggregateConfigLevelEnum level = AggregateConfigLevelEnum.findByCode(config.getLevel());

        // 按配置深度解析支付方式
        String method;
        String channelMchNo = null;
        String capability = null;

        switch (level) {
            case AUTO -> {
                // L1: 系统按场景推导支付方式, channelMchNo 为空走路由
                method = SceneMethodDefaultResolver.resolve(scene);
            }
            case METHOD -> {
                // L2: 每场景配置支付方式, channelMchNo 为空走路由
                GatewayAggregateScene sceneConfig = gatewayAggregateSceneManager
                        .findByConfigIdAndScene(config.getId(), scene.getCode());
                if (sceneConfig == null || StrUtil.isBlank(sceneConfig.getMethod())) {
                    throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                            "pay.error.gateway.sceneNotConfigured");
                }
                method = sceneConfig.getMethod();
            }
            case DIRECT -> {
                // L3: 直接指定通道商户号+能力, 走路由直定模式
                GatewayAggregateScene sceneConfig = gatewayAggregateSceneManager
                        .findByConfigIdAndScene(config.getId(), scene.getCode());
                if (sceneConfig == null || StrUtil.isBlank(sceneConfig.getChannelMchNo())) {
                    throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                            "pay.error.gateway.sceneNotConfigured");
                }
                channelMchNo = sceneConfig.getChannelMchNo();
                capability = sceneConfig.getCapability();
                // method: 优先用配置值, 未配则按场景推导(直定模式可由通道商户+能力反推)
                method = StrUtil.isNotBlank(sceneConfig.getMethod())
                        ? sceneConfig.getMethod()
                        : SceneMethodDefaultResolver.resolve(scene);
            }
            default -> throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.sceneNotSupport");
        }

        String clientIp = StrUtil.blankToDefault(param.getClientIp(), WebServletUtil.getClientIp());
        // product 传 null: AUTO/METHOD 由路由解析, DIRECT 由 channelMchNo 派生
        return gatewayPayHandleService.handle(order, null, method, channelMchNo, capability,
                param.getOpenId(), scene.getCode(), param.getDevice(), clientIp);
    }
}
