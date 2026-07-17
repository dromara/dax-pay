package cn.daxpay.open.payment.trade.runtime.service.pay.gateway;

import cn.daxpay.open.payment.merchant.dao.gateway.GatewayAggregateConfigManager;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayAggregateConfig;
import cn.daxpay.open.payment.merchant.enums.ClientEnvEnum;
import cn.daxpay.open.payment.merchant.enums.ClientRuntimeEnum;
import cn.daxpay.open.payment.merchant.service.gateway.ClientEnvPayResolveService;
import cn.daxpay.open.payment.trade.enums.GatewayPayTypeEnum;
import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.unipay.param.gateway.AggregateQrPayParam;
import cn.daxpay.open.payment.unipay.result.gateway.AggregatePayMetaResult;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayResult;
import cn.daxpay.open.platform.common.spring.util.WebServletUtil;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

/// # 聚合扫码支付服务
///
/// 按应用聚合配置的深度(level)解析支付方式, 委托 [ClientEnvPayResolveService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class AggregatePayService {

    /// 需要买家标识(openId/userId)的支付方式
    private static final Set<String> METHODS_NEED_OPEN_ID = Set.of(
            PayMethodEnum.WECHAT_JSAPI.getCode(),
            PayMethodEnum.WECHAT_MINI.getCode(),
            PayMethodEnum.ALIPAY_JSAPI.getCode(),
            PayMethodEnum.UNION_JSAPI.getCode(),
            PayMethodEnum.DOUYIN_JSAPI.getCode()
    );

    private final GatewayPayAssistService gatewayPayAssistService;
    private final ClientEnvPayResolveService clientEnvPayResolveService;
    private final GatewayPayHandleService gatewayPayHandleService;
    private final GatewayAggregateConfigManager aggregateConfigManager;

    /// H5 聚合元数据: autoLaunch / needOpenId(不下发敏感路由字段)
    public AggregatePayMetaResult getMeta(String orderNo, String clientEnvCode, String runtimeCode) {
        GatewayPayOrder order = gatewayPayAssistService.getOrderAndCheck(orderNo);
        if (!Objects.equals(order.getGatewayType(), GatewayPayTypeEnum.AGGREGATE.getCode())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.gateway.typeMismatch");
        }
        ClientEnvEnum clientEnv = ClientEnvEnum.findByCode(clientEnvCode);
        ClientRuntimeEnum runtime = ClientRuntimeEnum.ofOrDefault(runtimeCode);
        var resolved = clientEnvPayResolveService.resolveRequired(order.getAppId(), clientEnv, runtime);

        GatewayAggregateConfig config = aggregateConfigManager.findByAppId(order.getAppId())
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.gateway.aggregateConfigMissing"));

        return new AggregatePayMetaResult()
                .setAutoLaunch(Boolean.TRUE.equals(config.getAutoLaunch()))
                .setNeedOpenId(METHODS_NEED_OPEN_ID.contains(resolved.method()));
    }

    /// 聚合扫码发起支付
    public NormalPayResult aggregateQrPay(AggregateQrPayParam param) {
        GatewayPayOrder order = gatewayPayAssistService.getOrderAndCheck(param.getOrderNo());
        if (!Objects.equals(order.getGatewayType(), GatewayPayTypeEnum.AGGREGATE.getCode())) {
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
}
