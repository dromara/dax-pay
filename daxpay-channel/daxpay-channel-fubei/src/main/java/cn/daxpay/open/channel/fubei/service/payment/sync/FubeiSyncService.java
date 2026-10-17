package cn.daxpay.open.channel.fubei.service.payment.sync;

import cn.daxpay.open.channel.fubei.client.FubeiChannelClient;
import cn.daxpay.open.channel.fubei.client.credential.FubeiSdkCredential;
import cn.daxpay.open.channel.fubei.client.req.FubeiSyncReq;
import cn.daxpay.open.channel.fubei.client.resp.FubeiSyncResp;
import cn.daxpay.open.channel.fubei.code.FubeiCode;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 付呗支付同步业务服务
///
/// 通过 [FubeiChannelClient] 调用子应用查询付呗 fbpay.order.query 订单状态,
/// 将付呗订单状态(order_status: SUCCESS 成功 / 其他处理中)映射为平台 [PayFundStatusEnum]。
///
/// fbpay.order.query 支持按商户订单号(merchant_order_sn)查询, 无需等待付呗订单号回填。
@Slf4j
@Service
@RequiredArgsConstructor
public class FubeiSyncService {

    private final FubeiChannelClient fubeiChannelClient;

    /// 执行付呗支付同步
    public PaySyncResultBo sync(PayTrade trade, FubeiSdkCredential credential) {
        FubeiSyncReq req = new FubeiSyncReq();
        req.setOutTradeNo(trade.getTradeNo());
        req.setCredential(credential);

        DaxResult<FubeiSyncResp> result = fubeiChannelClient.sync(req);
        if (result.getCode() != 0) {
            log.error("付呗通道同步失败: outTradeNo={}, msg={}", trade.getTradeNo(), result.getMsg());
            return new PaySyncResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        return this.toSyncResult(result.getData());
    }

    /// 解析子应用响应
    private PaySyncResultBo toSyncResult(FubeiSyncResp resp) {
        PaySyncResultBo bo = new PaySyncResultBo();
        bo.setOutOrderNo(resp.getOutOrderNo());
        bo.setRealAmount(resp.getRealAmount());
        bo.setBuyerId(resp.getBuyerId());
        bo.setFinishTime(resp.getFinishTime());

        String tradeStatus = resp.getTradeState();
        if (StrUtil.isBlank(tradeStatus)) {
            return bo.setSyncSuccess(false)
                    .setSyncErrorMsg(StrUtil.blankToDefault(resp.getSyncData(), "付呗同步查询失败"));
        }

        // 付呗订单状态映射(SUCCESS 成功; 其余状态参考插件未穷举, 一律按处理中, 联调时按文档补齐)
        return switch (tradeStatus) {
            case FubeiCode.TRADE_STATUS_SUCCESS -> bo.setSyncSuccess(true).setPayStatus(PayFundStatusEnum.SUCCESS);
            default -> bo.setSyncSuccess(true).setPayStatus(PayFundStatusEnum.PROCESSING);
        };
    }
}
