package cn.daxpay.open.channel.shouqianba.service.payment.sync;

import cn.daxpay.open.channel.shouqianba.client.ShouqianbaChannelClient;
import cn.daxpay.open.channel.shouqianba.client.credential.ShouqianbaSdkCredential;
import cn.daxpay.open.channel.shouqianba.client.req.ShouqianbaSyncReq;
import cn.daxpay.open.channel.shouqianba.client.resp.ShouqianbaSyncResp;
import cn.daxpay.open.channel.shouqianba.code.ShouqianbaCode;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 收钱吧支付同步业务服务
///
/// 通过 [ShouqianbaChannelClient] 调用子应用按 client_sn 查询收钱吧订单状态,
/// 将收钱吧订单状态(order_status)映射为平台 [PayFundStatusEnum]。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShouqianbaSyncService {

    private final ShouqianbaChannelClient shouqianbaChannelClient;

    /// 执行收钱吧支付同步
    public PaySyncResultBo sync(PayTrade trade, ShouqianbaSdkCredential credential) {
        ShouqianbaSyncReq req = new ShouqianbaSyncReq();
        req.setOutTradeNo(trade.getTradeNo());
        req.setCredential(credential);

        DaxResult<ShouqianbaSyncResp> result = shouqianbaChannelClient.sync(req);
        if (result.getCode() != 0) {
            log.error("收钱吧通道同步失败: outTradeNo={}, msg={}", trade.getTradeNo(), result.getMsg());
            return new PaySyncResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        return toSyncResult(result.getData());
    }

    /// 解析子应用响应
    private PaySyncResultBo toSyncResult(ShouqianbaSyncResp resp) {
        PaySyncResultBo bo = new PaySyncResultBo();
        // 收钱吧流水号 sn 回写(下单响应不携带, 查单/回调补充后供退款使用)
        bo.setOutOrderNo(resp.getOutOrderNo());
        bo.setRealAmount(resp.getRealAmount());
        bo.setBuyerId(resp.getBuyerId());
        bo.setFinishTime(resp.getFinishTime());

        String tradeStatus = resp.getTradeState();
        if (StrUtil.isBlank(tradeStatus)) {
            return bo.setSyncSuccess(false)
                    .setSyncErrorMsg(StrUtil.blankToDefault(resp.getSyncData(), "收钱吧同步查询失败"));
        }

        // 收钱吧订单状态映射(PAID 已支付; REFUNDED/PARTIAL_REFUNDED 资金曾入账亦视为支付成功)
        return switch (tradeStatus) {
            case ShouqianbaCode.ORDER_STATUS_PAID,
                 ShouqianbaCode.ORDER_STATUS_REFUNDED,
                 ShouqianbaCode.ORDER_STATUS_PARTIAL_REFUNDED ->
                    bo.setSyncSuccess(true).setPayStatus(PayFundStatusEnum.SUCCESS);
            case ShouqianbaCode.ORDER_STATUS_FAIL, ShouqianbaCode.ORDER_STATUS_CANCELED ->
                    bo.setSyncSuccess(true).setPayStatus(PayFundStatusEnum.FAIL);
            default -> bo.setSyncSuccess(true).setPayStatus(PayFundStatusEnum.PROCESSING);
        };
    }
}
