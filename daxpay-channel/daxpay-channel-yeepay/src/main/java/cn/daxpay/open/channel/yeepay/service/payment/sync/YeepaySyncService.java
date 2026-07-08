package cn.daxpay.open.channel.yeepay.service.payment.sync;

import cn.daxpay.open.channel.yeepay.client.YeepayChannelClient;
import cn.daxpay.open.channel.yeepay.client.credential.YeepaySdkCredential;
import cn.daxpay.open.channel.yeepay.client.req.YeepaySyncReq;
import cn.daxpay.open.channel.yeepay.client.resp.YeepaySyncResp;
import cn.daxpay.open.payment.common.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.core.trade.bo.PaySyncResultBo;
import cn.daxpay.open.payment.core.trade.entity.PayTrade;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 易宝支付同步业务服务
///
/// 通过 [YeepayChannelClient] 调用子应用查询易宝订单状态,
/// 将统一状态码(SUCCESS/FAIL/CLOSED/PROGRESS)映射为平台 [PayFundStatusEnum]。
@Slf4j
@Service
@RequiredArgsConstructor
public class YeepaySyncService {

    private final YeepayChannelClient yeepayChannelClient;

    /// 执行易宝支付同步
    public PaySyncResultBo sync(PayTrade trade, YeepaySdkCredential credential) {
        YeepaySyncReq req = new YeepaySyncReq();
        req.setOutTradeNo(trade.getTradeNo());
        req.setCredential(credential);

        DaxResult<YeepaySyncResp> result = yeepayChannelClient.sync(req);
        if (result.getCode() != 0) {
            log.error("易宝通道同步失败: outTradeNo={}, msg={}", trade.getTradeNo(), result.getMsg());
            return new PaySyncResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        return toSyncResult(result.getData());
    }

    /// 解析子应用响应
    private PaySyncResultBo toSyncResult(YeepaySyncResp resp) {
        PaySyncResultBo bo = new PaySyncResultBo();
        bo.setOutOrderNo(resp.getTradeNo());
        bo.setRealAmount(resp.getRealAmount());
        bo.setBuyerId(resp.getBuyerId());
        bo.setFinishTime(resp.getFinishTime());

        String tradeStatus = resp.getTradeStatus();
        if (StrUtil.isBlank(tradeStatus)) {
            return bo.setSyncSuccess(false)
                    .setSyncErrorMsg(StrUtil.blankToDefault(resp.getSyncData(), "易宝同步查询失败"));
        }

        // 统一状态码映射
        return switch (tradeStatus) {
            case "SUCCESS" -> bo.setSyncSuccess(true).setPayStatus(PayFundStatusEnum.SUCCESS);
            case "FAIL" -> bo.setSyncSuccess(true).setPayStatus(PayFundStatusEnum.FAIL);
            case "CLOSED" -> bo.setSyncSuccess(true).setPayStatus(PayFundStatusEnum.CLOSE);
            default -> bo.setSyncSuccess(true).setPayStatus(PayFundStatusEnum.PROCESSING);
        };
    }
}
