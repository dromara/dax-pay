package cn.daxpay.open.payment.unipay.trade.service;

import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.payment.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayQueryParam;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayOrderResult;
import cn.daxpay.open.payment.unipay.trade.convert.UnipayNormalPayOrderConvert;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 普通支付订单查询服务(对外)
///
/// 基于 core 双表(NormalPayOrder 业务容器 + PayTrade 资金凭证)实现对外订单查询
/// 支持按平台交易号(orderNo 对应 tradeNo)或商户业务单号(bizOrderNo)查询
@Slf4j
@Service
@RequiredArgsConstructor
public class NormalPayOrderQueryService {

    private final NormalPayOrderManager normalPayOrderManager;
    private final PayTradeManager payTradeManager;

    /// 查询支付订单
    public NormalPayOrderResult queryPayOrder(NormalPayQueryParam param) {
        // 校验参数, 支付订单号和商户订单号不能都为空
        if (StrUtil.isBlank(param.getOrderNo()) && Objects.isNull(param.getBizOrderNo())) {
            // 支付: 支付订单号不能都为空
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.orderNoRequired");
        }
        NormalPayOrder order;
        // 优先按平台交易号(orderNo = tradeNo)查询
        if (StrUtil.isNotBlank(param.getOrderNo())) {
            PayTrade trade = payTradeManager.findByTradeNo(param.getOrderNo())
                    // 支付: 支付订单不存在
                    .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
            order = normalPayOrderManager.findById(trade.getContainerId())
                    // 支付: 支付订单不存在
                    .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        } else {
            // 按商户业务单号查询
            order = normalPayOrderManager.findByBizOrderNo(param.getBizOrderNo(), param.getAppId())
                    .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        }
        // 同名映射业务容器，再联表资金凭证补充/覆盖交易字段
        NormalPayOrderResult result = UnipayNormalPayOrderConvert.CONVERT.toResult(order);
        PayTrade trade = payTradeManager.findByContainerId(order.getId(),
                cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum.NORMAL.getCode()).orElse(null);
        if (Objects.nonNull(trade)) {
            result.setTradeNo(trade.getTradeNo());
            result.setOutOrderNo(trade.getOutOrderNo());
            // 对外契约 status = 资金态，覆盖容器业务 status
            result.setStatus(trade.getStatus());
            result.setRefundableBalance(trade.getRefundableBalance());
        }
        return result;
    }
}
