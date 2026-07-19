package cn.daxpay.open.channel.lakala.service.payment;

import cn.daxpay.open.channel.lakala.client.LakalaChannelClient;
import cn.daxpay.open.channel.lakala.client.credential.LakalaSdkCredential;
import cn.daxpay.open.channel.lakala.client.req.LakalaCloseReq;
import cn.daxpay.open.channel.lakala.client.resp.LakalaCloseResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 拉卡拉服务商关单业务服务
///
/// 通过 [LakalaChannelClient] 调用子应用关闭拉卡拉订单。
/// 拉卡拉仅提供关单接口(`/v3/labs/relation/close`), 无撤销接口, useCancel 参数忽略。
@Slf4j
@Service
@RequiredArgsConstructor
public class LakalaCloseService {

    private final LakalaChannelClient lakalaChannelClient;

    /// 关闭订单
    ///
    /// @param order      支付订单
    /// @param credential 通道凭证
    /// @param useCancel  是否撤销(拉卡拉不支持撤销, 忽略此参数)
    /// @param clientIp   客户端IP(取自原下单订单, 透传至拉卡拉 location_info.request_ip)
    /// @return 关闭类型(恒为 CLOSE)
    public CloseTypeEnum close(PayTrade order, LakalaSdkCredential credential, boolean useCancel, String clientIp) {
        LakalaCloseReq req = new LakalaCloseReq();
        req.setCredential(credential);
        req.setOriginOutTradeNo(order.getTradeNo());
        req.setOriginTradeNo(order.getOutOrderNo());
        req.setClientIp(clientIp);

        DaxResult<LakalaCloseResp> result = lakalaChannelClient.close(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.lakala.closeFailed", result.getMsg());
        }
        // 拉卡拉只有关单, 返回 CLOSE
        return CloseTypeEnum.CLOSE;
    }
}
