package cn.daxpay.open.payment.trade.runtime.service.pay.gateway;

import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.trade.order.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.unipay.param.gateway.GatewayOrderQueryParam;
import cn.daxpay.open.payment.unipay.result.gateway.GatewayOrderResult;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 网关订单查询服务
@Service
@RequiredArgsConstructor
public class GatewayOrderQueryService {

    private final GatewayPayOrderManager gatewayPayOrderManager;
    private final PayTradeManager payTradeManager;
    private final MerchantContextLoader merchantContextLoader;

    /// 按 orderNo 或 bizOrderNo 查询网关订单
    public GatewayOrderResult query(GatewayOrderQueryParam param) {
        if (StrUtil.isBlank(param.getOrderNo()) && StrUtil.isBlank(param.getBizOrderNo())) {
            // 支付: 支付订单号不能都为空
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.orderNoRequired");
        }
        GatewayPayOrder order = null;
        if (StrUtil.isNotBlank(param.getOrderNo())) {
            order = gatewayPayOrderManager.findByOrderNo(param.getOrderNo()).orElse(null);
        }
        if (order == null && StrUtil.isNotBlank(param.getBizOrderNo())) {
            // 商户维度定位(bizOrderNo 同商户唯一), mchNo 必传由参数校验保证
            order = gatewayPayOrderManager.findByBizOrderNoAndMch(param.getBizOrderNo(), param.getMchNo()).orElse(null);
        }
        if (order == null) {
            // 支付: 支付订单不存在
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.payOrderNotExist");
        }
        // 归属校验: orderNo 为全局唯一编号, 防跨商户查单
        if (!Objects.equals(order.getMchNo(), param.getMchNo())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.orderNotBelong");
        }
        return this.toResult(order);
    }

    /// H5 侧按 orderNo 引导查询
    ///
    /// Manager 引导读可跨租户定位订单；装载 mchNo 后附属 Trade 走租户内查询。
    public GatewayOrderResult queryByOrderNoNotTenant(String orderNo) {
        GatewayPayOrder order = gatewayPayOrderManager.findByOrderNoNotTenant(orderNo)
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.payOrderNotExist"));
        merchantContextLoader.initMch(order.getMchNo());
        return this.toResult(order);
    }

    /// 网关订单实体 → 查询结果 DTO(含关联资金交易号)
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
        payTradeManager.findByContainerId(order.getId(), PayTradeTypeEnum.GATEWAY.getCode())
                .ifPresent(trade -> result.setTradeNo(trade.getTradeNo())
                        .setOutOrderNo(trade.getOutOrderNo())
                        .setFundStatus(trade.getStatus()));
        return result;
    }
}
