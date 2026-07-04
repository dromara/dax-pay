package cn.daxpay.open.channel.wechat.service.payment.close;

import cn.daxpay.open.channel.wechat.client.WechatChannelClient;
import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.client.req.WechatCloseReq;
import cn.daxpay.open.channel.wechat.client.resp.WechatCloseResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.core.trade.entity.PayTrade;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 微信支付关闭业务服务
///
/// 通过 [WechatChannelClient] 调用子应用 dax-pay-channel-one 关闭微信订单。
/// 微信 V3 仅提供关单接口(closeOrderV3), 无撤销接口(useCancel 参数对微信无意义, 统一返回 [CloseTypeEnum.CLOSE]);
/// 关闭失败的网关状态兜底(订单已关闭/不存在)由子应用内部处理。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatCloseService {

    private final WechatChannelClient wechatChannelClient;

    /// 执行微信订单关闭
    ///
    /// @param trade      支付订单(tradeNo 作为 out_trade_no, outOrderNo 作为 transaction_id)
    /// @param credential 通道调用凭证
    /// @param useCancel  是否使用撤销方式(微信无撤销接口, 忽略)
    /// @return 关闭方式(微信固定返回 [CloseTypeEnum.CLOSE])
    public CloseTypeEnum close(PayTrade trade, WechatSdkCredential credential, boolean useCancel) {
        // 构建请求
        WechatCloseReq req = new WechatCloseReq();
        req.setOutTradeNo(trade.getTradeNo());
        req.setTransactionId(trade.getOutOrderNo());
        req.setCredential(credential);

        // 调用子应用
        DaxResult<WechatCloseResp> result = wechatChannelClient.close(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.OPERATION_FAIL, "error.channel.wechat.closeFailed", result.getMsg());
        }

        // 微信仅支持关闭, 无撤销
        return CloseTypeEnum.CLOSE;
    }
}
