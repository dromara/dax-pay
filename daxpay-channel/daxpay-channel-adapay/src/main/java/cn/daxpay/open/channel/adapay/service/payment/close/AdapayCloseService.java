package cn.daxpay.open.channel.adapay.service.payment.close;

import cn.daxpay.open.channel.adapay.client.AdapayChannelClient;
import cn.daxpay.open.channel.adapay.client.credential.AdapaySdkCredential;
import cn.daxpay.open.channel.adapay.client.req.AdapayCloseReq;
import cn.daxpay.open.channel.adapay.client.resp.AdapayCloseResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.core.trade.entity.PayTrade;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Adapay 支付关闭业务服务
///
/// 通过 [AdapayChannelClient] 调用子应用关闭Adapay 订单。
/// 关单需用Adapay 支付对象 ID(PayTrade.outOrderNo)。
@Slf4j
@Service
@RequiredArgsConstructor
public class AdapayCloseService {

    private final AdapayChannelClient adapayChannelClient;

    /// 执行Adapay 订单关闭
    public CloseTypeEnum close(PayTrade trade, AdapaySdkCredential credential, boolean useCancel) {
        AdapayCloseReq req = new AdapayCloseReq();
        req.setOutTradeNo(trade.getTradeNo());
        // Adapay 支付对象 ID
        req.setPaymentId(trade.getOutOrderNo());
        req.setCredential(credential);

        DaxResult<AdapayCloseResp> result = adapayChannelClient.close(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.OPERATION_FAIL, "channel.error.adapayCloseFailed", result.getMsg());
        }

        return useCancel ? CloseTypeEnum.CANCEL : CloseTypeEnum.CLOSE;
    }
}
