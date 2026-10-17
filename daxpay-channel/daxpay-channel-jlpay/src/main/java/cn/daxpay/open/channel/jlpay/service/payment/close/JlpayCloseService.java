package cn.daxpay.open.channel.jlpay.service.payment.close;

import cn.daxpay.open.channel.jlpay.client.JlpayChannelClient;
import cn.daxpay.open.channel.jlpay.client.credential.JlpaySdkCredential;
import cn.daxpay.open.channel.jlpay.client.req.JlpayCloseReq;
import cn.daxpay.open.channel.jlpay.client.resp.JlpayCloseResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 嘉联支付关闭业务服务
///
/// 通过 [JlpayChannelClient] 调用子应用走 /open/trans/cancel 撤销嘉联订单。
/// 嘉联仅有撤销接口(无独立关单接口), 撤销需原嘉联交易号(ori_transaction_id);
/// 通道交易号未知(未收到下单回执/回调)时无法发起撤销, 退化为本地关单(二维码到时效自然失效)。
@Slf4j
@Service
@RequiredArgsConstructor
public class JlpayCloseService {

    private final JlpayChannelClient jlpayChannelClient;

    /// 关闭订单
    ///
    /// @param order      支付订单
    /// @param credential 通道凭证
    /// @param useCancel  是否撤销(嘉联仅有撤销接口, 忽略此参数恒走撤销)
    /// @param clientIp   客户端IP(上送 mch_create_ip)
    /// @return 关闭类型(恒为 CANCEL, 嘉联语义为当日交易撤销)
    public CloseTypeEnum close(PayTrade order, JlpaySdkCredential credential, boolean useCancel, String clientIp) {
        // 嘉联交易号未知时无法撤销, 本地关单
        if (StrUtil.isBlank(order.getOutOrderNo())) {
            log.info("嘉联本地关单(通道交易号未知, 二维码到时效自然失效): tradeNo={}", order.getTradeNo());
            return useCancel ? CloseTypeEnum.CANCEL : CloseTypeEnum.CLOSE;
        }
        JlpayCloseReq req = new JlpayCloseReq();
        req.setCredential(credential);
        req.setOutTradeNo(order.getTradeNo());
        req.setOutOrderNo(order.getOutOrderNo());
        req.setClientIp(clientIp);

        DaxResult<JlpayCloseResp> result = jlpayChannelClient.close(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.jlpay.closeFailed", result.getMsg());
        }
        // 嘉联仅提供撤销接口, 返回 CANCEL
        return CloseTypeEnum.CANCEL;
    }
}
