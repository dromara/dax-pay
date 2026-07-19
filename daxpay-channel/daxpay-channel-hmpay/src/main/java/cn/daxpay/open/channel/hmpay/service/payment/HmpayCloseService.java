package cn.daxpay.open.channel.hmpay.service.payment;

import cn.daxpay.open.channel.hmpay.client.HmpayChannelClient;
import cn.daxpay.open.channel.hmpay.client.credential.HmpaySdkCredential;
import cn.daxpay.open.channel.hmpay.client.req.HmpayCloseReq;
import cn.daxpay.open.channel.hmpay.client.resp.HmpayCloseResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 河马付服务商关单业务服务
///
/// 通过 [HmpayChannelClient] 调用子应用关闭河马付(杉德)订单。
/// 杉德仅提供关单接口(trade.close), 无撤销接口。
@Slf4j
@Service
@RequiredArgsConstructor
public class HmpayCloseService {

    private final HmpayChannelClient hmpayChannelClient;

    /// 关闭订单
    ///
    /// @param order      支付订单
    /// @param credential 通道凭证
    /// @return 关闭类型(恒为 CLOSE)
    public CloseTypeEnum close(PayTrade order, HmpaySdkCredential credential) {
        HmpayCloseReq req = new HmpayCloseReq();
        req.setCredential(credential);
        // 商户订单号(原 out_order_no)
        req.setOutTradeNo(order.getTradeNo());

        DaxResult<HmpayCloseResp> result = hmpayChannelClient.close(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.hmpay.closeFailed", result.getMsg());
        }
        return CloseTypeEnum.CLOSE;
    }
}
