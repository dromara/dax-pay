package cn.daxpay.open.channel.sand.service.payment.sync;

import cn.daxpay.open.channel.sand.client.SandChannelClient;
import cn.daxpay.open.channel.sand.client.credential.SandSdkCredential;
import cn.daxpay.open.channel.sand.client.req.SandSyncReq;
import cn.daxpay.open.channel.sand.client.resp.SandSyncResp;
import cn.daxpay.open.channel.sand.code.SandCode;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 杉德支付同步业务服务
///
/// 通过 [SandChannelClient] 调用子应用查询杉德订单状态(trans.order.query, 按商户订单号查询),
/// 将杉德订单状态(orderStatus: success 成功 / fail 失败 / 其他处理中)映射为平台 [PayFundStatusEnum]。
@Slf4j
@Service
@RequiredArgsConstructor
public class SandSyncService {

    private final SandChannelClient sandChannelClient;

    /// 执行杉德支付同步
    public PaySyncResultBo sync(PayTrade trade, SandSdkCredential credential) {
        SandSyncReq req = new SandSyncReq();
        req.setOutTradeNo(trade.getTradeNo());
        req.setCredential(credential);

        DaxResult<SandSyncResp> result = sandChannelClient.sync(req);
        if (result.getCode() != 0) {
            log.error("杉德通道同步失败: outTradeNo={}, msg={}", trade.getTradeNo(), result.getMsg());
            return new PaySyncResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        return toSyncResult(result.getData());
    }

    /// 解析子应用响应
    private PaySyncResultBo toSyncResult(SandSyncResp resp) {
        PaySyncResultBo bo = new PaySyncResultBo();
        bo.setOutOrderNo(resp.getTradeNo());
        bo.setRealAmount(resp.getAmount());
        bo.setBuyerId(resp.getBuyerId());
        bo.setFinishTime(resp.getFinishTime());

        String tradeStatus = resp.getTradeState();
        if (StrUtil.isBlank(tradeStatus)) {
            return bo.setSyncSuccess(false)
                    .setSyncErrorMsg(StrUtil.blankToDefault(resp.getSyncData(), "杉德同步查询失败"));
        }

        // 杉德订单状态映射
        return switch (tradeStatus) {
            case SandCode.ORDER_STATUS_SUCCESS -> bo.setSyncSuccess(true).setPayStatus(PayFundStatusEnum.SUCCESS);
            case SandCode.ORDER_STATUS_FAIL -> bo.setSyncSuccess(true).setPayStatus(PayFundStatusEnum.FAIL);
            default -> bo.setSyncSuccess(true).setPayStatus(PayFundStatusEnum.PROCESSING);
        };
    }
}
