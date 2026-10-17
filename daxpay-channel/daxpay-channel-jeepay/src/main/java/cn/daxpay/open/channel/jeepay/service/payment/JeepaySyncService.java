package cn.daxpay.open.channel.jeepay.service.payment;

import cn.daxpay.open.channel.jeepay.client.JeepayChannelClient;
import cn.daxpay.open.channel.jeepay.client.credential.JeepaySdkCredential;
import cn.daxpay.open.channel.jeepay.client.req.JeepaySyncReq;
import cn.daxpay.open.channel.jeepay.client.resp.JeepaySyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # Jeepay订单同步业务服务
///
/// 通过 [JeepayChannelClient] 调用子应用查询 Jeepay 订单状态。
///
/// Jeepay 状态映射: "2"=支付成功, "5"=已退款(原单已支付后退款, 资金状态视为成功),
/// "3"=支付失败/"4"已撤销/"6"已关闭(终态失败), 其他("1"支付中)=处理中。
@Slf4j
@Service
@RequiredArgsConstructor
public class JeepaySyncService {

    /// Jeepay 状态: 支付成功(终态成功)
    private static final String PAY_STATE_SUCCESS = "2";

    /// Jeepay 状态: 已退款(原单已支付后退款, 资金状态视为成功)
    private static final String PAY_STATE_REFUNDED = "5";

    /// Jeepay 状态: 支付失败(终态失败)
    private static final String PAY_STATE_FAIL = "3";

    /// Jeepay 状态: 已撤销(终态失败)
    private static final String PAY_STATE_REVOKED = "4";

    /// Jeepay 状态: 已关闭(终态失败)
    private static final String PAY_STATE_CLOSED = "6";

    private final JeepayChannelClient jeepayChannelClient;

    /// 同步订单状态
    public PaySyncResultBo sync(PayTrade order, JeepaySdkCredential credential) {
        JeepaySyncReq req = new JeepaySyncReq();
        req.setCredential(credential);
        req.setOutTradeNo(order.getTradeNo());
        req.setTradeNo(order.getOutOrderNo());

        DaxResult<JeepaySyncResp> result = jeepayChannelClient.sync(req);
        PaySyncResultBo bo = new PaySyncResultBo();
        if (result.getCode() != 0) {
            // 同步失败(不抛异常, 由核心层决定重试)
            bo.setSyncSuccess(false);
            bo.setSyncErrorMsg(result.getMsg());
            return bo;
        }

        JeepaySyncResp resp = result.getData();
        bo.setSyncSuccess(true);
        bo.setSyncData(resp.getSyncData());
        bo.setOutOrderNo(resp.getTradeNo());
        bo.setPayStatus(mapTradeState(resp.getTradeState()));
        // 成功态补充金额/时间
        if (isSuccessState(resp.getTradeState())) {
            bo.setAmount(resp.getTotalAmount());
            bo.setRealAmount(resp.getTotalAmount());
            bo.setFinishTime(resp.getFinishTime());
        }
        return bo;
    }

    /// Jeepay 交易状态(原始码) → 平台资金状态
    private PayFundStatusEnum mapTradeState(String tradeState) {
        // 已支付 / 已退款(原单已支付后退款, 资金状态视为成功)
        if (isSuccessState(tradeState)) {
            return PayFundStatusEnum.SUCCESS;
        }
        // 终态失败: 3=支付失败 / 4=已撤销 / 6=已关闭
        if (Objects.equals(tradeState, PAY_STATE_FAIL)
                || Objects.equals(tradeState, PAY_STATE_REVOKED)
                || Objects.equals(tradeState, PAY_STATE_CLOSED)) {
            return PayFundStatusEnum.FAIL;
        }
        // 其他状态(1=支付中)视为处理中
        return PayFundStatusEnum.PROCESSING;
    }

    /// 是否成功态(2=成功, 5=已退款)
    private static boolean isSuccessState(String tradeState) {
        return Objects.equals(tradeState, PAY_STATE_SUCCESS)
                || Objects.equals(tradeState, PAY_STATE_REFUNDED);
    }
}
