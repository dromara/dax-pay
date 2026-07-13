package cn.daxpay.open.payment.unipay.gateway.service;

import cn.daxpay.open.payment.common.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.gateway.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.core.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.gateway.entity.GatewayPayOrder;
import cn.daxpay.open.payment.core.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.unipay.gateway.param.GatewayOrderQueryParam;
import cn.daxpay.open.payment.unipay.gateway.result.GatewayOrderResult;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 网关订单查询服务
@Service
@RequiredArgsConstructor
public class GatewayOrderQueryService {

    private final GatewayPayOrderManager gatewayPayOrderManager;
    private final PayTradeManager payTradeManager;

    public GatewayOrderResult query(GatewayOrderQueryParam param) {
        if (StrUtil.isBlank(param.getOrderNo()) && StrUtil.isBlank(param.getBizOrderNo())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.orderNoRequired");
        }
        GatewayPayOrder order = null;
        if (StrUtil.isNotBlank(param.getOrderNo())) {
            order = gatewayPayOrderManager.findByOrderNo(param.getOrderNo()).orElse(null);
        }
        if (order == null && StrUtil.isNotBlank(param.getBizOrderNo())) {
            if (StrUtil.isBlank(param.getAppId())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "validation.field.appId.notBlank");
            }
            order = gatewayPayOrderManager.findByBizOrderNo(param.getBizOrderNo(), param.getAppId()).orElse(null);
        }
        if (order == null) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.payOrderNotExist");
        }
        return this.toResult(order);
    }

    /// H5 侧按 orderNo 查(忽略租户)
    public GatewayOrderResult queryByOrderNoNotTenant(String orderNo) {
        GatewayPayOrder order = gatewayPayOrderManager.findByOrderNoNotTenant(orderNo)
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.payOrderNotExist"));
        return this.toResult(order);
    }

    public GatewayOrderResult toResult(GatewayPayOrder order) {
        GatewayOrderResult result = new GatewayOrderResult()
                .setOrderNo(order.getOrderNo())
                .setBizOrderNo(order.getBizOrderNo())
                .setGatewayType(order.getGatewayType())
                .setTitle(order.getTitle())
                .setDescription(order.getDescription())
                .setAmount(order.getAmount())
                .setCurrency(order.getCurrency())
                .setStatus(order.getStatus())
                .setExpiredTime(order.getExpiredTime())
                .setPayTime(order.getPayTime())
                .setChannel(order.getChannel())
                .setMethod(order.getMethod())
                .setProduct(order.getProduct())
                .setAttach(order.getAttach())
                .setReturnUrl(order.getReturnUrl());
        PayTrade trade = payTradeManager.findByContainerId(order.getId(), PayTradeTypeEnum.GATEWAY.getCode())
                .orElse(null);
        if (trade != null) {
            result.setTradeNo(trade.getTradeNo())
                    .setOutOrderNo(trade.getOutOrderNo())
                    .setFundStatus(trade.getStatus());
        }
        return result;
    }
}
