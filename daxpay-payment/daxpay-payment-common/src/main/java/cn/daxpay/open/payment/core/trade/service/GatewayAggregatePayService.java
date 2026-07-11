package cn.daxpay.open.payment.core.trade.service;

import cn.daxpay.open.payment.common.enums.CashierSceneEnum;
import cn.daxpay.open.payment.common.enums.GatewayPayTypeEnum;
import cn.daxpay.open.payment.core.trade.entity.GatewayPayOrder;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayAggregateConfig;
import cn.daxpay.open.payment.merchant.service.gateway.GatewayAggregateConfigService;
import cn.daxpay.open.payment.unipay.param.gateway.AggregateQrPayParam;
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
@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayAggregatePayService {

    private final GatewayPayAssistService gatewayPayAssistService;
    private final GatewayAggregateConfigService gatewayAggregateConfigService;
    private final GatewayPayHandleService gatewayPayHandleService;

    /// 聚合扫码发起支付
    public NormalPayResult aggregateQrPay(AggregateQrPayParam param) {
        GatewayPayOrder order = gatewayPayAssistService.getOrderAndCheck(param.getOrderNo());
        if (!Objects.equals(order.getGatewayType(), GatewayPayTypeEnum.AGGREGATE.getCode())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.gateway.typeMismatch");
        }
        CashierSceneEnum scene = CashierSceneEnum.findByCode(param.getScene());
        GatewayAggregateConfig config = gatewayAggregateConfigService.getRequiredByAppId(order.getAppId());
        String product;
        String method;
        switch (scene) {
            case WECHAT_PAY -> {
                product = config.getWxProduct();
                method = config.getWxMethod();
            }
            case ALIPAY -> {
                product = config.getAlipayProduct();
                method = config.getAlipayMethod();
            }
            case UNION_PAY -> {
                product = config.getUnionProduct();
                method = config.getUnionMethod();
            }
            default -> throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.sceneNotSupport");
        }
        if (StrUtil.isBlank(product) || StrUtil.isBlank(method)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.sceneNotConfigured");
        }
        String clientIp = StrUtil.blankToDefault(param.getClientIp(), WebServletUtil.getClientIp());
        return gatewayPayHandleService.handle(order, product, method,
                param.getOpenId(), scene.getCode(), param.getDevice(), clientIp);
    }
}
