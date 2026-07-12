package cn.daxpay.open.channel.hkrt.service.payment;

import cn.daxpay.open.channel.hkrt.client.HkrtChannelClient;
import cn.daxpay.open.channel.hkrt.client.credential.HkrtSdkCredential;
import cn.daxpay.open.channel.hkrt.client.req.HkrtCloseReq;
import cn.daxpay.open.channel.hkrt.client.resp.HkrtCloseResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.core.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 海科融通服务商关单业务服务
///
/// 通过 [HkrtChannelClient] 调用子应用关闭海科融通订单。
/// 海科融通仅提供关单接口, 无撤销接口, useCancel 参数忽略。
@Slf4j
@Service
@RequiredArgsConstructor
public class HkrtCloseService {

    private final HkrtChannelClient hkrtChannelClient;

    /// 关闭订单
    ///
    /// @param order      支付订单
    /// @param credential 通道凭证
    /// @param useCancel  是否撤销(海科融通不支持撤销, 忽略此参数)
    /// @param clientIp   客户端IP(取自原下单订单, 透传至海科融通)
    /// @return 关闭类型(恒为 CLOSE)
    public CloseTypeEnum close(PayTrade order, HkrtSdkCredential credential, boolean useCancel, String clientIp) {
        HkrtCloseReq req = new HkrtCloseReq();
        req.setCredential(credential);
        req.setOriginOutTradeNo(order.getTradeNo());
        req.setOriginTradeNo(order.getOutOrderNo());
        req.setClientIp(clientIp);

        DaxResult<HkrtCloseResp> result = hkrtChannelClient.close(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "channel.error.hkrtCloseFailed", result.getMsg());
        }
        // 海科融通只有关单, 返回 CLOSE
        return CloseTypeEnum.CLOSE;
    }
}
