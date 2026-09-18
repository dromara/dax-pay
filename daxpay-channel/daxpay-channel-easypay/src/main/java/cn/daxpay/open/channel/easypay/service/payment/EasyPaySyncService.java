package cn.daxpay.open.channel.easypay.service.payment;

import cn.daxpay.open.channel.easypay.client.EasyPayChannelClient;
import cn.daxpay.open.channel.easypay.client.credential.EasyPaySdkCredential;
import cn.daxpay.open.channel.easypay.client.req.EasyPaySyncReq;
import cn.daxpay.open.channel.easypay.client.resp.EasyPaySyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 易支付订单同步业务服务
///
/// 通过 [EasyPayChannelClient] 调用子应用查询易支付订单状态。
///
/// 易支付状态映射: "1"=已支付(成功), "2"=已退款(原单已支付后退款, 资金状态视为成功), 其他=支付中。
@Slf4j
@Service
@RequiredArgsConstructor
public class EasyPaySyncService {

    /// 易支付状态: 已支付(终态成功)
    private static final String STATUS_TRADE_SUCCESS = "1";

    /// 易支付状态: 已退款(原单已支付后退款, 资金状态视为成功)
    private static final String STATUS_TRADE_REFUNDED = "2";

    private final EasyPayChannelClient easyPayChannelClient;

    /// 同步订单状态
    public PaySyncResultBo sync(PayTrade order, EasyPaySdkCredential credential) {
        EasyPaySyncReq req = new EasyPaySyncReq();
        req.setCredential(credential);
        req.setOutTradeNo(order.getTradeNo());
        req.setTradeNo(order.getOutOrderNo());

        DaxResult<EasyPaySyncResp> result = easyPayChannelClient.sync(req);
        PaySyncResultBo bo = new PaySyncResultBo();
        if (result.getCode() != 0) {
            // 同步失败(不抛异常, 由核心层决定重试)
            bo.setSyncSuccess(false);
            bo.setSyncErrorMsg(result.getMsg());
            return bo;
        }

        EasyPaySyncResp resp = result.getData();
        bo.setSyncSuccess(true);
        bo.setSyncData(resp.getSyncData());
        bo.setOutOrderNo(resp.getTradeNo());
        bo.setPayStatus(mapTradeState(resp.getTradeState()));
        // 成功态补充金额/时间/买家
        if (isSuccessState(resp.getTradeState())) {
            bo.setAmount(resp.getTotalAmount());
            bo.setRealAmount(resp.getTotalAmount());
            bo.setFinishTime(resp.getFinishTime());
            bo.setBuyerId(resp.getBuyerId());
        }
        return bo;
    }

    /// 易支付交易状态(原始码) → 平台资金状态
    private PayFundStatusEnum mapTradeState(String tradeState) {
        // 已支付 / 已退款(原单已支付后退款, 资金状态视为成功)
        if (isSuccessState(tradeState)) {
            return PayFundStatusEnum.SUCCESS;
        }
        // 其他状态视为支付中
        return PayFundStatusEnum.PROCESSING;
    }

    /// 是否成功态(1=已支付, 2=已退款)
    private static boolean isSuccessState(String tradeState) {
        return Objects.equals(tradeState, STATUS_TRADE_SUCCESS)
                || Objects.equals(tradeState, STATUS_TRADE_REFUNDED);
    }
}
