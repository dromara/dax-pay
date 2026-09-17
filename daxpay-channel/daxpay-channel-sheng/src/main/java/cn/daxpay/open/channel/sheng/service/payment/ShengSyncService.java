package cn.daxpay.open.channel.sheng.service.payment;

import cn.daxpay.open.channel.sheng.client.ShengChannelClient;
import cn.daxpay.open.channel.sheng.client.credential.ShengSdkCredential;
import cn.daxpay.open.channel.sheng.client.req.ShengSyncReq;
import cn.daxpay.open.channel.sheng.client.resp.ShengSyncResp;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 盛付通订单同步业务服务
///
/// 通过 [ShengChannelClient] 调用子应用查询盛付通订单状态。
///
/// 盛付通交易状态映射(PAY_INIT/PAY_ING=处理中, PAY_SUCCESS=成功, PAY_FAIL=失败, CLOSED=关闭,
/// REFUND 族=原单已支付后退款, 资金状态视为成功); 同时兼容子应用标准化状态值(SUCCESS/FAIL/CLOSED)。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShengSyncService {

    private final ShengChannelClient shengChannelClient;

    /// 同步订单状态
    public PaySyncResultBo sync(PayTrade order, ShengSdkCredential credential) {
        ShengSyncReq req = new ShengSyncReq();
        req.setCredential(credential);
        req.setOutTradeNo(order.getTradeNo());
        req.setTradeNo(order.getOutOrderNo());

        DaxResult<ShengSyncResp> result = shengChannelClient.sync(req);
        PaySyncResultBo bo = new PaySyncResultBo();
        if (result.getCode() != 0) {
            // 同步失败(不抛异常, 由核心层决定重试)
            bo.setSyncSuccess(false);
            bo.setSyncErrorMsg(result.getMsg());
            return bo;
        }

        ShengSyncResp resp = result.getData();
        bo.setSyncSuccess(true);
        bo.setSyncData(resp.getSyncData());
        bo.setOutOrderNo(resp.getTradeNo());
        bo.setPayStatus(mapTradeState(resp.getTradeState()));
        // 成功时补充金额/时间/买家
        if (Objects.equals(resp.getTradeState(), "PAY_SUCCESS") || Objects.equals(resp.getTradeState(), "SUCCESS")
                || isRefundState(resp.getTradeState())) {
            bo.setAmount(resp.getTotalAmount());
            bo.setRealAmount(resp.getTotalAmount());
            bo.setFinishTime(resp.getFinishTime());
            bo.setBuyerId(resp.getBuyerId());
        }
        return bo;
    }

    /// 盛付通交易状态 → 平台资金状态
    ///
    /// 同时兼容盛付通原始状态(PAY_*)与子应用标准化状态(SUCCESS/FAIL/CLOSED)。
    private PayFundStatusEnum mapTradeState(String tradeState) {
        // 支付成功
        if (Objects.equals(tradeState, "PAY_SUCCESS") || Objects.equals(tradeState, "SUCCESS")) {
            return PayFundStatusEnum.SUCCESS;
        }
        // 支付失败
        if (Objects.equals(tradeState, "PAY_FAIL") || Objects.equals(tradeState, "FAIL")) {
            return PayFundStatusEnum.FAIL;
        }
        // 已关闭
        if (Objects.equals(tradeState, "CLOSED")) {
            return PayFundStatusEnum.CLOSE;
        }
        // REFUND 族: 原订单已支付后发起退款, 资金状态视为成功
        if (isRefundState(tradeState)) {
            return PayFundStatusEnum.SUCCESS;
        }
        // 处理中(PAY_INIT / PAY_ING 及其他)
        return PayFundStatusEnum.PROCESSING;
    }

    /// 是否退款族状态(REFUND 前缀, 表示原单已支付后进入退款流程)
    private static boolean isRefundState(String tradeState) {
        return Objects.nonNull(tradeState) && tradeState.startsWith("REFUND");
    }
}
