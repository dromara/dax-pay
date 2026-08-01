package cn.daxpay.open.channel.union.service.payment.sync;

import cn.daxpay.open.channel.union.client.UnionChannelClient;
import cn.daxpay.open.channel.union.client.credential.UnionSdkCredential;
import cn.daxpay.open.channel.union.client.enums.UnionPayMethod;
import cn.daxpay.open.channel.union.client.req.UnionSyncReq;
import cn.daxpay.open.channel.union.client.resp.UnionSyncResp;
import cn.daxpay.open.channel.union.code.UnionCode;
import cn.daxpay.open.channel.union.util.UnionDateUtil;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 云闪付支付同步业务服务
///
/// 通过 [UnionChannelClient] 调用子应用查询银联订单状态,
/// 将统一状态码(SUCCESS/PROGRESS/CLOSED)映射为平台 [PayFundStatusEnum]。
///
/// 银联交易凭证 queryId 作为通道订单号存入 [PaySyncResultBo#getOutOrderNo],
/// 退款时作为 origQryId 使用。
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionSyncService {

    private final UnionChannelClient unionChannelClient;

    /// 执行云闪付支付同步
    public PaySyncResultBo sync(PayTrade trade, UnionSdkCredential credential, UnionPayMethod method) {
        UnionSyncReq req = new UnionSyncReq();
        req.setOutTradeNo(trade.getTradeNo());
        req.setMethod(method);
        req.setCredential(credential);

        DaxResult<UnionSyncResp> result = unionChannelClient.sync(req);
        if (result.getCode() != 0) {
            log.error("云闪付通道同步失败: outTradeNo={}, msg={}", trade.getTradeNo(), result.getMsg());
            return new PaySyncResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }
        return toSyncResult(result.getData());
    }

    /// 解析子应用响应
    private PaySyncResultBo toSyncResult(UnionSyncResp resp) {
        PaySyncResultBo bo = new PaySyncResultBo();
        // 银联 queryId 作为通道订单号(退款时作为 origQryId, 由框架存入 trade.outOrderNo)
        bo.setOutOrderNo(resp.getQueryId());
        bo.setRealAmount(resp.getTotalAmount());
        bo.setBuyerId(resp.getBuyerId());
        bo.setFinishTime(UnionDateUtil.parseCst(resp.getPayTime()));

        String tradeStatus = resp.getTradeStatus();
        if (StrUtil.isBlank(tradeStatus)) {
            return bo.setSyncSuccess(false)
                    .setSyncErrorMsg(StrUtil.blankToDefault(resp.getErrorMsg(), "云闪付同步查询失败"));
        }
        return switch (tradeStatus) {
            case UnionCode.TRADE_STATUS_SUCCESS -> bo.setPayStatus(PayFundStatusEnum.SUCCESS);
            case UnionCode.TRADE_STATUS_PROGRESS -> bo.setPayStatus(PayFundStatusEnum.PROCESSING);
            case UnionCode.TRADE_STATUS_CLOSED -> bo.setPayStatus(PayFundStatusEnum.CLOSE);
            default -> bo.setSyncSuccess(false).setSyncErrorMsg("云闪付未知交易状态: " + tradeStatus);
        };
    }
}
