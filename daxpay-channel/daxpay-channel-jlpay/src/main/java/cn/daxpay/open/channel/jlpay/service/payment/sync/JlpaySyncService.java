package cn.daxpay.open.channel.jlpay.service.payment.sync;

import cn.daxpay.open.channel.jlpay.client.JlpayChannelClient;
import cn.daxpay.open.channel.jlpay.client.credential.JlpaySdkCredential;
import cn.daxpay.open.channel.jlpay.client.req.JlpaySyncReq;
import cn.daxpay.open.channel.jlpay.client.resp.JlpaySyncResp;
import cn.daxpay.open.channel.jlpay.code.JlpayCode;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 嘉联支付同步业务服务
///
/// 通过 [JlpayChannelClient] 调用子应用查询嘉联 /open/trans/chnquery 订单状态,
/// 将嘉联支付状态(status: 2 成功 / 其他未成功)与交易处理状态(tranSts: 1 处理中, 非 1 失败)
/// 映射为平台 [PayFundStatusEnum]。
///
/// chnquery 支持按商户订单号查询, 平台交易号即可直查, 无需等待通道交易号回填。
@Slf4j
@Service
@RequiredArgsConstructor
public class JlpaySyncService {

    private final JlpayChannelClient jlpayChannelClient;

    /// 执行嘉联支付同步
    public PaySyncResultBo sync(PayTrade trade, JlpaySdkCredential credential) {
        JlpaySyncReq req = new JlpaySyncReq();
        req.setOutTradeNo(trade.getTradeNo());
        req.setOutOrderNo(trade.getOutOrderNo());
        req.setCredential(credential);

        DaxResult<JlpaySyncResp> result = jlpayChannelClient.sync(req);
        if (result.getCode() != 0) {
            log.error("嘉联通道同步失败: outTradeNo={}, msg={}", trade.getTradeNo(), result.getMsg());
            return new PaySyncResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        return toSyncResult(result.getData());
    }

    /// 解析子应用响应
    private PaySyncResultBo toSyncResult(JlpaySyncResp resp) {
        PaySyncResultBo bo = new PaySyncResultBo();
        bo.setOutOrderNo(resp.getOutOrderNo());
        bo.setRealAmount(resp.getRealAmount());
        bo.setBuyerId(resp.getBuyerId());
        bo.setFinishTime(resp.getFinishTime());

        String tradeStatus = resp.getTradeState();
        if (StrUtil.isBlank(tradeStatus)) {
            return bo.setSyncSuccess(false)
                    .setSyncErrorMsg(StrUtil.blankToDefault(resp.getSyncData(), "嘉联同步查询失败"));
        }

        // 嘉联支付状态映射: status=2 成功; status 非 2 且 tranSts 非 1 视为失败终态, 其余处理中
        if (JlpayCode.TRADE_STATUS_SUCCESS.equals(tradeStatus)) {
            return bo.setSyncSuccess(true).setPayStatus(PayFundStatusEnum.SUCCESS);
        }
        if (!JlpayCode.TRAN_STS_PROCESSING.equals(resp.getTranSts())) {
            return bo.setSyncSuccess(true).setPayStatus(PayFundStatusEnum.FAIL);
        }
        return bo.setSyncSuccess(true).setPayStatus(PayFundStatusEnum.PROCESSING);
    }
}
