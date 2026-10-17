package cn.daxpay.open.channel.ysep.service.payment;

import cn.daxpay.open.channel.ysep.client.YsepChannelClient;
import cn.daxpay.open.channel.ysep.client.credential.YsepSdkCredential;
import cn.daxpay.open.channel.ysep.client.req.YsepRefundReq;
import cn.daxpay.open.channel.ysep.client.resp.YsepRefundResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 银盛退款执行业务服务(两产品共用)
///
/// 通过 [YsepChannelClient] 调用子应用完成银盛退款。
/// 银盛退款接口同步返回终态, 成功即 complete=true, 失败由子应用抛异常。
@Slf4j
@Service
@RequiredArgsConstructor
public class YsepRefundService {

    private final YsepChannelClient ysepChannelClient;

    /// 执行退款
    public RefundResultBo refund(RefundOrder refundOrder, YsepSdkCredential credential, ProductEnum product) {
        YsepRefundReq req = new YsepRefundReq();
        req.setCredential(credential);
        req.setProduct(product.getCode());
        // 退款单号(作为银盛商户退款单号, 幂等键)
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        // 原支付订单号(平台 tradeNo 作为原商户订单号)
        req.setOriginOutTradeNo(refundOrder.getTradeNo());
        // 原通道交易号(首次退款时可能为空)
        req.setOriginTradeNo(refundOrder.getOutOrderNo());
        req.setAmount(refundOrder.getAmount());
        req.setReason(refundOrder.getReason());

        DaxResult<YsepRefundResp> result = ysepChannelClient.refund(req);
        if (result.getCode() != 0) {
            // 银盛退款异常
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.ysep.refundFailed", result.getMsg());
        }

        YsepRefundResp resp = result.getData();
        RefundResultBo bo = new RefundResultBo()
                .setOutRefundNo(resp.getTradeNo())
                .setFinishTime(resp.getFinishTime())
                .setComplete(Boolean.TRUE.equals(resp.getComplete()));
        // 退款状态: 同步成功视为成功, 否则处理中(理论上不出现, 银盛退款为同步终态)
        bo.setStatus(Boolean.TRUE.equals(resp.getComplete())
                ? RefundOrderStatusEnum.SUCCESS
                : RefundOrderStatusEnum.PROGRESS);
        return bo;
    }
}
