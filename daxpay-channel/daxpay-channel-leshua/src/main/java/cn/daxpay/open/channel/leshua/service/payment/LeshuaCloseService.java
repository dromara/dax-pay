package cn.daxpay.open.channel.leshua.service.payment;

import cn.daxpay.open.channel.leshua.client.LeshuaChannelClient;
import cn.daxpay.open.channel.leshua.client.credential.LeshuaSdkCredential;
import cn.daxpay.open.channel.leshua.client.req.LeshuaCloseReq;
import cn.daxpay.open.channel.leshua.client.resp.LeshuaCloseResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.core.trade.entity.PayTrade;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 乐刷服务商关单业务服务
///
/// 通过 [LeshuaChannelClient] 调用子应用关闭乐刷订单。
/// 乐刷关单走 `get_tdcode` + `jspay_flag=2`, useCancel 参数忽略。
@Slf4j
@Service
@RequiredArgsConstructor
public class LeshuaCloseService {

    private final LeshuaChannelClient leshuaChannelClient;

    /// 关闭订单
    ///
    /// @param order      支付订单
    /// @param credential 通道凭证
    /// @param useCancel  是否撤销(乐刷不支持撤销, 忽略此参数)
    /// @param clientIp   客户端IP
    /// @return 关闭类型(恒为 CLOSE)
    public CloseTypeEnum close(PayTrade order, LeshuaSdkCredential credential, boolean useCancel, String clientIp) {
        LeshuaCloseReq req = new LeshuaCloseReq();
        req.setCredential(credential);
        req.setLeshuaOrderId(order.getOutOrderNo());
        req.setOutTradeNo(order.getTradeNo());
        req.setClientIp(clientIp);

        DaxResult<LeshuaCloseResp> result = leshuaChannelClient.close(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "channel.error.leshuaCloseFailed", result.getMsg());
        }
        return CloseTypeEnum.CLOSE;
    }
}
