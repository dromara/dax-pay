package cn.daxpay.open.channel.yeepay.service.payment.close;

import cn.daxpay.open.channel.yeepay.client.YeepayChannelClient;
import cn.daxpay.open.channel.yeepay.client.credential.YeepaySdkCredential;
import cn.daxpay.open.channel.yeepay.client.req.YeepayCloseReq;
import cn.daxpay.open.channel.yeepay.client.resp.YeepayCloseResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.core.trade.entity.PayTrade;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 易宝支付关闭业务服务
///
/// 通过 [YeepayChannelClient] 调用子应用关闭易宝订单。
@Slf4j
@Service
@RequiredArgsConstructor
public class YeepayCloseService {

    private final YeepayChannelClient yeepayChannelClient;

    /// 执行易宝订单关闭
    public CloseTypeEnum close(PayTrade trade, YeepaySdkCredential credential, boolean useCancel) {
        YeepayCloseReq req = new YeepayCloseReq();
        req.setOutTradeNo(trade.getTradeNo());
        req.setCredential(credential);

        DaxResult<YeepayCloseResp> result = yeepayChannelClient.close(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.OPERATION_FAIL, "channel.error.yeepayCloseFailed", result.getMsg());
        }

        return useCancel ? CloseTypeEnum.CANCEL : CloseTypeEnum.CLOSE;
    }
}
