package cn.daxpay.open.channel.llianpay.service.payment.sync;

import cn.daxpay.open.channel.llianpay.client.LlianpayChannelClient;
import cn.daxpay.open.channel.llianpay.client.credential.LlianpaySdkCredential;
import cn.daxpay.open.channel.llianpay.client.req.LlianpaySyncReq;
import cn.daxpay.open.channel.llianpay.client.resp.LlianpaySyncResp;
import cn.daxpay.open.channel.llianpay.code.LlianpayCode;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 连连支付同步业务服务
///
/// 通过 [LlianpayChannelClient] 调用子应用查询连连订单状态,
/// 将连连交易状态(txn_status: SUCCESS 成功 / FAILURE 失败 / CANCEL 已退回 / 其他处理中)
/// 映射为平台 [PayFundStatusEnum]。
///
/// 连连查单按商户单号(txn_seqno = 平台交易号), 无需通道订单号, 任意时点可查。
@Slf4j
@Service
@RequiredArgsConstructor
public class LlianpaySyncService {

    private final LlianpayChannelClient llianpayChannelClient;

    /// 执行连连支付同步
    public PaySyncResultBo sync(PayTrade trade, LlianpaySdkCredential credential) {
        LlianpaySyncReq req = new LlianpaySyncReq();
        req.setOutTradeNo(trade.getTradeNo());
        req.setCredential(credential);

        DaxResult<LlianpaySyncResp> result = llianpayChannelClient.sync(req);
        if (result.getCode() != 0) {
            log.error("连连通道同步失败: outTradeNo={}, msg={}", trade.getTradeNo(), result.getMsg());
            return new PaySyncResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        return toSyncResult(result.getData());
    }

    /// 解析子应用响应
    private PaySyncResultBo toSyncResult(LlianpaySyncResp resp) {
        PaySyncResultBo bo = new PaySyncResultBo();
        bo.setOutOrderNo(resp.getOutOrderNo());
        bo.setRealAmount(resp.getRealAmount());
        bo.setBuyerId(resp.getBuyerId());
        bo.setFinishTime(resp.getFinishTime());

        String tradeStatus = resp.getTradeState();
        if (StrUtil.isBlank(tradeStatus)) {
            return bo.setSyncSuccess(false)
                    .setSyncErrorMsg(StrUtil.blankToDefault(resp.getSyncData(), "连连同步查询失败"));
        }

        // 连连交易状态映射
        return switch (tradeStatus) {
            case LlianpayCode.TRADE_STATUS_SUCCESS -> bo.setSyncSuccess(true).setPayStatus(PayFundStatusEnum.SUCCESS);
            case LlianpayCode.TRADE_STATUS_FAIL, LlianpayCode.TRADE_STATUS_CANCEL ->
                    bo.setSyncSuccess(true).setPayStatus(PayFundStatusEnum.FAIL);
            default -> bo.setSyncSuccess(true).setPayStatus(PayFundStatusEnum.PROCESSING);
        };
    }
}
