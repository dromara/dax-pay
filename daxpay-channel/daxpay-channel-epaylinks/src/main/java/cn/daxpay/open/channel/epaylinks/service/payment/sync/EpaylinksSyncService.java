package cn.daxpay.open.channel.epaylinks.service.payment.sync;

import cn.daxpay.open.channel.epaylinks.client.EpaylinksChannelClient;
import cn.daxpay.open.channel.epaylinks.client.credential.EpaylinksSdkCredential;
import cn.daxpay.open.channel.epaylinks.client.req.EpaylinksSyncReq;
import cn.daxpay.open.channel.epaylinks.client.resp.EpaylinksSyncResp;
import cn.daxpay.open.channel.epaylinks.code.EpaylinksCode;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 易票联支付同步业务服务
///
/// 通过 [EpaylinksChannelClient] 调用子应用查询易票联 PaymentQuery 订单状态,
/// 将易票联交易状态(payState: 00 成功 / 01 失败 / 其他处理中)映射为平台 [PayFundStatusEnum]。
@Slf4j
@Service
@RequiredArgsConstructor
public class EpaylinksSyncService {

    private final EpaylinksChannelClient epaylinksChannelClient;

    /// 执行易票联支付同步
    public PaySyncResultBo sync(PayTrade trade, EpaylinksSdkCredential credential) {
        EpaylinksSyncReq req = new EpaylinksSyncReq();
        req.setOutTradeNo(trade.getTradeNo());
        req.setCredential(credential);

        DaxResult<EpaylinksSyncResp> result = epaylinksChannelClient.sync(req);
        if (result.getCode() != 0) {
            log.error("易票联通道同步失败: outTradeNo={}, msg={}", trade.getTradeNo(), result.getMsg());
            return new PaySyncResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        return toSyncResult(result.getData());
    }

    /// 解析子应用响应
    private PaySyncResultBo toSyncResult(EpaylinksSyncResp resp) {
        PaySyncResultBo bo = new PaySyncResultBo();
        bo.setOutOrderNo(resp.getOutOrderNo());
        bo.setRealAmount(resp.getRealAmount());
        bo.setBuyerId(resp.getBuyerId());
        bo.setFinishTime(resp.getFinishTime());

        String tradeStatus = resp.getTradeState();
        if (StrUtil.isBlank(tradeStatus)) {
            return bo.setSyncSuccess(false)
                    .setSyncErrorMsg(StrUtil.blankToDefault(resp.getSyncData(), "易票联同步查询失败"));
        }

        // 易票联交易状态映射(00 成功 / 01 失败 / 其他处理中)
        return switch (tradeStatus) {
            case EpaylinksCode.TRADE_STATUS_SUCCESS -> bo.setSyncSuccess(true).setPayStatus(PayFundStatusEnum.SUCCESS);
            case EpaylinksCode.TRADE_STATUS_FAIL -> bo.setSyncSuccess(true).setPayStatus(PayFundStatusEnum.FAIL);
            default -> bo.setSyncSuccess(true).setPayStatus(PayFundStatusEnum.PROCESSING);
        };
    }
}
