package cn.daxpay.open.channel.lakala.service.moss;

import cn.daxpay.open.channel.lakala.client.LakalaChannelClient;
import cn.daxpay.open.channel.lakala.client.credential.MossSdkCredential;
import cn.daxpay.open.channel.lakala.client.req.MossCloseReq;
import cn.daxpay.open.channel.lakala.client.resp.MossCloseResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 拉卡拉 MOSS 关单业务服务
///
/// 通过 [LakalaChannelClient] 调用子应用关闭 MOSS 订单。
///
/// MOSS 未提供远端关单接口, 子应用按「查单确认非成功即视为关闭」处理
/// (下单时上送 order_eff_time 自然过期兜底), 无撤销语义, useCancel 参数忽略。
@Slf4j
@Service
@RequiredArgsConstructor
public class LakalaMossCloseService {

    private final LakalaChannelClient lakalaChannelClient;

    /// 关闭订单
    ///
    /// @param order      支付订单
    /// @param credential 通道凭证
    /// @param useCancel  是否撤销(MOSS 不支持撤销, 忽略此参数)
    /// @return 关闭类型(恒为 CLOSE)
    public CloseTypeEnum close(PayTrade order, MossSdkCredential credential, boolean useCancel) {
        MossCloseReq req = new MossCloseReq();
        req.setCredential(credential);
        req.setOutTradeNo(order.getTradeNo());

        DaxResult<MossCloseResp> result = lakalaChannelClient.mossClose(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.lakala.closeFailed", result.getMsg());
        }
        // MOSS 无撤销语义, 恒返回 CLOSE
        return CloseTypeEnum.CLOSE;
    }
}
