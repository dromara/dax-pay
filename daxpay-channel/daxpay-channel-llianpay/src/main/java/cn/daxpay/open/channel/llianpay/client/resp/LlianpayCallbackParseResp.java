package cn.daxpay.open.channel.llianpay.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 连连通道回调验签解析响应(主应用侧)
///
/// 支付回调与退款回调共用本对象, 由 [#tradeType] 区分(PAY/REFUND), 字段按场景取值;
/// [#ackBody] 为连连规约应答串(Success/Fail), 主应用原样返回给连连。
@Data
@Accessors(chain = true)
public class LlianpayCallbackParseResp {

    /// 验签是否通过 + 解析是否成功
    private Boolean success;

    /// 回调类型(PAY 支付回调 / REFUND 退款回调)
    private String tradeType;

    /// 商户侧单号(支付回调 = txn_seqno 原支付交易号; 退款回调 = refund_seqno 退款单号)
    private String outTradeNo;

    /// 连连平台流水号(支付回调 = platform_txno; 退款回调 = platform_refundno)
    private String outOrderNo;

    /// 交易状态(支付回调 txn_status; 退款回调 refund_status, 原始码)
    private String tradeStatus;

    /// 金额(单位: 分; 支付回调 order_amount / 退款回调 refund_amount, 元换算分)
    private Long amount;

    /// 完成时间(success_time)
    private OffsetDateTime finishTime;

    /// 买家标识(支付回调 chnl_user_id)
    private String buyerId;

    /// 渠道交易单号(支付回调 chnl_txno)
    private String outTransNo;

    /// 连连规约应答串(Success/Fail, 主应用原样回给连连)
    private String ackBody;

    /// 错误描述(验签失败或解析异常时填充)
    private String errorMsg;

    /// 原始回调报文(用于对账/排障)
    private String syncData;
}
