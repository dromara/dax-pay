package cn.daxpay.open.channel.ums.service.payment.sync;

import cn.daxpay.open.channel.ums.client.UmsChannelClient;
import cn.daxpay.open.channel.ums.client.credential.UmsSdkCredential;
import cn.daxpay.open.channel.ums.client.enums.UmsPayMethod;
import cn.daxpay.open.channel.ums.client.req.UmsSyncReq;
import cn.daxpay.open.channel.ums.client.resp.UmsSyncResp;
import cn.daxpay.open.channel.ums.code.UmsCode;
import cn.daxpay.open.channel.ums.util.UmsDateUtil;
import cn.daxpay.open.payment.common.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 银联商务支付同步业务服务
///
/// 通过 [UmsChannelClient] 调用子应用查询银联商务订单状态,
/// 将统一状态码(SUCCESS/PROGRESS/CLOSED)映射为平台 [PayFundStatusEnum]。
@Slf4j
@Service
@RequiredArgsConstructor
public class UmsSyncService {

    private final UmsChannelClient umsChannelClient;

    /// 执行银联商务支付同步
    public PaySyncResultBo sync(PayTrade trade, UmsSdkCredential credential) {
        UmsSyncReq req = new UmsSyncReq();
        req.setOutTradeNo(trade.getTradeNo());
        // 原订单创建时间(UTC), 子应用按通道时区转换为银联商务 billDate(yyyy-MM-dd)
        req.setBillDate(trade.getCreateTime());
        // 首期默认扫码查询
        req.setMethod(UmsPayMethod.QRCODE);
        req.setCredential(credential);

        DaxResult<UmsSyncResp> result = umsChannelClient.sync(req);
        if (result.getCode() != 0) {
            log.error("银联商务通道同步失败: outTradeNo={}, msg={}", trade.getTradeNo(), result.getMsg());
            return new PaySyncResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        return toSyncResult(result.getData());
    }

    /// 解析子应用响应
    private PaySyncResultBo toSyncResult(UmsSyncResp resp) {
        PaySyncResultBo bo = new PaySyncResultBo();
        bo.setOutOrderNo(resp.getTargetOrderId());
        // 金额
        bo.setRealAmount(resp.getTotalAmount());
        // 买家标识
        bo.setBuyerId(resp.getBuyerId());
        // 支付成功时间(银联商务返回东八区本地时间, 由 UmsDateUtil 解析为带偏移的 OffsetDateTime)
        bo.setFinishTime(UmsDateUtil.parseCst(resp.getPayTime()));

        String tradeStatus = resp.getTradeStatus();
        if (StrUtil.isBlank(tradeStatus)) {
            return bo.setSyncSuccess(false)
                    .setSyncErrorMsg(StrUtil.blankToDefault(resp.getErrorMsg(), "银联商务同步查询失败"));
        }

        // 统一状态码映射
        return switch (tradeStatus) {
            case UmsCode.TRADE_STATUS_SUCCESS -> bo.setPayStatus(PayFundStatusEnum.SUCCESS);
            case UmsCode.TRADE_STATUS_PROGRESS -> bo.setPayStatus(PayFundStatusEnum.PROCESSING);
            case UmsCode.TRADE_STATUS_CLOSED -> bo.setPayStatus(PayFundStatusEnum.CLOSE);
            default -> bo.setSyncSuccess(false).setSyncErrorMsg("银联商务未知交易状态: " + tradeStatus);
        };
    }
}
