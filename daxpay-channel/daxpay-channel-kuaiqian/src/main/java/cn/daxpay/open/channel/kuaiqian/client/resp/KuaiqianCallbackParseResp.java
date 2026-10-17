package cn.daxpay.open.channel.kuaiqian.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 快钱通道回调验签解析响应(主应用侧)
///
/// 子应用对快钱异步通知解密验签后回传; 支付/退款回调共用, 由 [#tradeType] 区分(PAY/REFUND);
/// [#ackBody] 为 A9005 规约加密加签应答报文, 主应用原样返回给快钱。
@Data
@Accessors(chain = true)
public class KuaiqianCallbackParseResp {

    /// 验签是否通过 + 解析是否成功
    private Boolean success;

    /// 回调类型(PAY 支付回调 / REFUND 退款回调)
    private String tradeType;

    /// 商户侧单号(支付回调 = 原支付交易号; 退款回调 = 退款单号)
    private String outTradeNo;

    /// 快钱订单控制号(idOrderCtrl)
    private String outOrderNo;

    /// 交易状态(支付回调 orderStatus; 退款回调为退款结果状态)
    private String tradeStatus;

    /// 金额(单位: 分)
    private Long amount;

    /// 完成时间(timeEnd)
    private OffsetDateTime finishTime;

    /// 买家标识(支付回调 thirdPartyBuyerId)
    private String buyerId;

    /// 通道外部交易号(支付回调 channelTradeNo)
    private String outTransNo;

    /// A9005 应答报文(加密加签后的 JSON, 主应用原样回给快钱)
    private String ackBody;

    /// 错误描述(验签失败或解析异常时填充)
    private String errorMsg;

    /// 原始回调明文(解密后的 body, 用于对账/排障)
    private String syncData;
}
