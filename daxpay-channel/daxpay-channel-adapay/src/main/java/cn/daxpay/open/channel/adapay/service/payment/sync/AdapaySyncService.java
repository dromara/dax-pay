package cn.daxpay.open.channel.adapay.service.payment.sync;

import cn.daxpay.open.channel.adapay.client.AdapayChannelClient;
import cn.daxpay.open.channel.adapay.client.credential.AdapaySdkCredential;
import cn.daxpay.open.channel.adapay.client.req.AdapaySyncReq;
import cn.daxpay.open.channel.adapay.client.resp.AdapaySyncResp;
import cn.daxpay.open.channel.adapay.code.AdapayCode;
import cn.daxpay.open.channel.adapay.util.AdapayDateUtil;
import cn.daxpay.open.payment.common.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.core.trade.bo.PaySyncResultBo;
import cn.daxpay.open.payment.core.trade.entity.PayTrade;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 汇付天下支付同步业务服务
///
/// 通过 [AdapayChannelClient] 调用子应用查询汇付天下订单状态,
/// 将统一状态码(SUCCESS/PROGRESS/CLOSED)映射为平台 [PayFundStatusEnum]。
///
/// 注意: 查询需用汇付支付对象 ID(PayTrade.outOrderNo), 由支付下单时回写。
@Slf4j
@Service
@RequiredArgsConstructor
public class AdapaySyncService {

    private final AdapayChannelClient adapayChannelClient;

    /// 执行汇付天下支付同步
    public PaySyncResultBo sync(PayTrade trade, AdapaySdkCredential credential) {
        AdapaySyncReq req = new AdapaySyncReq();
        req.setOutTradeNo(trade.getTradeNo());
        // 汇付支付对象 ID(支付下单时回写到 PayTrade.outOrderNo)
        req.setPaymentId(trade.getOutOrderNo());
        req.setCredential(credential);

        DaxResult<AdapaySyncResp> result = adapayChannelClient.sync(req);
        if (result.getCode() != 0) {
            log.error("汇付天下通道同步失败: outTradeNo={}, msg={}", trade.getTradeNo(), result.getMsg());
            return new PaySyncResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        return toSyncResult(result.getData());
    }

    /// 解析子应用响应
    private PaySyncResultBo toSyncResult(AdapaySyncResp resp) {
        PaySyncResultBo bo = new PaySyncResultBo();
        bo.setRealAmount(resp.getTotalAmount());
        bo.setBuyerId(resp.getBuyerId());
        bo.setFinishTime(AdapayDateUtil.parse(resp.getPayTime()));

        String tradeStatus = resp.getTradeStatus();
        if (StrUtil.isBlank(tradeStatus)) {
            return bo.setSyncSuccess(false)
                    .setSyncErrorMsg(StrUtil.blankToDefault(resp.getErrorMsg(), "汇付天下同步查询失败"));
        }

        // 统一状态码映射
        return switch (tradeStatus) {
            case AdapayCode.TRADE_STATUS_SUCCESS -> bo.setPayStatus(PayFundStatusEnum.SUCCESS);
            case AdapayCode.TRADE_STATUS_PROGRESS -> bo.setPayStatus(PayFundStatusEnum.PROCESSING);
            case AdapayCode.TRADE_STATUS_CLOSED -> bo.setPayStatus(PayFundStatusEnum.CLOSE);
            default -> bo.setSyncSuccess(false).setSyncErrorMsg("汇付天下未知交易状态: " + tradeStatus);
        };
    }
}
