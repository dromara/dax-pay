package cn.daxpay.open.channel.helipay.service.payment;

import cn.daxpay.open.channel.helipay.client.HelipayChannelClient;
import cn.daxpay.open.channel.helipay.client.credential.HelipaySdkCredential;
import cn.daxpay.open.channel.helipay.client.req.HelipaySyncReq;
import cn.daxpay.open.channel.helipay.client.resp.HelipaySyncResp;
import cn.daxpay.open.channel.helipay.code.HelipayCode;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 合利宝订单同步业务服务
///
/// 通过 [HelipayChannelClient] 调用子应用查询合利宝订单状态(AppPayQuery, 按商户订单号)。
///
/// 状态映射: rt7_orderStatus=SUCCESS → 成功; 其他状态(未支付/处理中/已关闭等)
/// 沿用 PHP 参考语义按非成功处理(不强行判失败, 置处理中由框架按超时策略兜底)。
@Slf4j
@Service
@RequiredArgsConstructor
public class HelipaySyncService {

    private final HelipayChannelClient helipayChannelClient;

    /// 同步订单状态
    public PaySyncResultBo sync(PayTrade order, HelipaySdkCredential credential) {
        HelipaySyncReq req = new HelipaySyncReq();
        req.setCredential(credential);
        req.setOutTradeNo(order.getTradeNo());

        DaxResult<HelipaySyncResp> result = helipayChannelClient.sync(req);
        if (result.getCode() != 0) {
            // 同步失败(不抛异常, 由核心层决定重试)
            return new PaySyncResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }
        return toSyncResult(result.getData());
    }

    /// 解析子应用响应
    private PaySyncResultBo toSyncResult(HelipaySyncResp resp) {
        PaySyncResultBo bo = new PaySyncResultBo();
        bo.setSyncSuccess(true);
        bo.setSyncData(resp.getSyncData());
        bo.setOutOrderNo(resp.getOutOrderNo());
        bo.setBuyerId(resp.getOpenId());
        // 合利宝交易状态映射(SUCCESS=成功, 其他按处理中, 沿用 PHP 参考非成功即未支付语义)
        if (HelipayCode.TRADE_STATUS_SUCCESS.equals(resp.getTradeState())) {
            bo.setPayStatus(PayFundStatusEnum.SUCCESS);
            // 成功态补充金额/时间
            bo.setAmount(resp.getTotalAmount());
            bo.setRealAmount(resp.getTotalAmount());
            bo.setFinishTime(resp.getFinishTime());
        } else {
            bo.setPayStatus(PayFundStatusEnum.PROCESSING);
        }
        return bo;
    }
}
