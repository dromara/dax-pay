package cn.daxpay.open.channel.fubei.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 付呗通道回调验签解析响应(主应用侧)
///
/// 子应用对付呗异步通知(form 参数 + MD5 签名)验签解析后回传给主应用。
/// 支付回调与退款回调共用本对象, 由 [#tradeType] 区分(当前仅 PAY, 付呗无退款异步通知)。
///
/// 回调应答为纯文本约定(主应用侧常量直出, 不由子应用生成): 成功处理 `success` / 验签失败 `fail`。
@Data
@Accessors(chain = true)
public class FubeiCallbackParseResp {

    /// 验签是否通过 + 解析是否成功
    private Boolean success;

    /// 回调类型(PAY 支付回调)
    private String tradeType;

    /// 商户侧单号(merchant_order_sn = 平台支付交易号)
    private String outTradeNo;

    /// 付呗订单号(order_sn)
    private String outOrderNo;

    /// 订单状态(order_status 原始码)
    private String tradeStatus;

    /// 金额(单位: 分; total_amount 元转分)
    private Long amount;

    /// 完成时间(finish_time)
    private OffsetDateTime finishTime;

    /// 买家标识(user_id)
    private String buyerId;

    /// 渠道外部交易号(channel_order_sn)
    private String outTransNo;

    /// 错误描述(验签失败或解析异常时填充)
    private String errorMsg;

    /// 回调业务明文(data 节点 JSON, 用于对账/排障)
    private String syncData;
}
