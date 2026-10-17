package cn.daxpay.open.channel.cpcn.service.payment.sync;

import cn.daxpay.open.channel.cpcn.client.CpcnChannelClient;
import cn.daxpay.open.channel.cpcn.client.credential.CpcnSdkCredential;
import cn.daxpay.open.channel.cpcn.client.req.CpcnSyncReq;
import cn.daxpay.open.channel.cpcn.client.resp.CpcnSyncResp;
import cn.daxpay.open.channel.cpcn.code.CpcnCode;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 中金支付同步业务服务
///
/// 通过 [CpcnChannelClient] 调用子应用查询中金 5016 交易状态,
/// 将中金交易状态(Status: 10 处理中 / 30 成功 / 40 失败)映射为平台 [PayFundStatusEnum]。
///
/// 5016 按商户流水号 TxSN(下单时上送的平台支付交易号)查询, 不依赖通道订单号, 下单后即可查。
@Slf4j
@Service
@RequiredArgsConstructor
public class CpcnSyncService {

    private final CpcnChannelClient cpcnChannelClient;

    /// 执行中金支付同步
    public PaySyncResultBo sync(PayTrade trade, CpcnSdkCredential credential) {
        CpcnSyncReq req = new CpcnSyncReq();
        req.setOutTradeNo(trade.getTradeNo());
        req.setCredential(credential);

        DaxResult<CpcnSyncResp> result = cpcnChannelClient.sync(req);
        if (result.getCode() != 0) {
            log.error("中金通道同步失败: outTradeNo={}, msg={}", trade.getTradeNo(), result.getMsg());
            return new PaySyncResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        return toSyncResult(result.getData());
    }

    /// 解析子应用响应
    private PaySyncResultBo toSyncResult(CpcnSyncResp resp) {
        PaySyncResultBo bo = new PaySyncResultBo();
        bo.setOutOrderNo(resp.getOutOrderNo());
        bo.setRealAmount(resp.getRealAmount());
        bo.setFinishTime(resp.getFinishTime());
        bo.setBuyerId(resp.getBuyerId());

        String tradeStatus = resp.getTradeState();
        if (StrUtil.isBlank(tradeStatus)) {
            return bo.setSyncSuccess(false)
                    .setSyncErrorMsg(StrUtil.blankToDefault(resp.getSyncData(), "中金同步查询失败"));
        }

        // 中金交易状态映射
        return switch (tradeStatus) {
            case CpcnCode.TRADE_STATUS_SUCCESS -> bo.setSyncSuccess(true).setPayStatus(PayFundStatusEnum.SUCCESS);
            case CpcnCode.TRADE_STATUS_FAIL -> bo.setSyncSuccess(true).setPayStatus(PayFundStatusEnum.FAIL);
            default -> bo.setSyncSuccess(true).setPayStatus(PayFundStatusEnum.PROCESSING);
        };
    }
}
