package cn.daxpay.open.channel.xingyi.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 星驿付通道回调验签解析响应(主应用侧)
///
/// 子应用对星驿付异步通知验签解析后回传; 星驿付仅有支付回调(tradeType 恒为 PAY);
/// [#ackBody] 为星驿付约定的 JSON 应答串, 主应用原样返回给星驿付。
@Data
@Accessors(chain = true)
public class XingyiCallbackParseResp {

    /// 验签是否通过 + 解析是否成功
    private Boolean success;

    /// 回调类型(恒为 PAY)
    private String tradeType;

    /// 商户订单号(THREE_ORDER_NO = 主应用支付交易号)
    private String outTradeNo;

    /// 星驿付订单号(ORDER_NO)
    private String tradeNo;

    /// 交易金额(单位: 分, TXAMT 字段, 若回调携带)
    private Long amount;

    /// 交易状态(标准化: ORDER_STATUS=1 → SUCCESS, 其他 → FAIL)
    private String tradeStatus;

    /// 完成时间(ORDER_TIME, 东八区)
    private OffsetDateTime finishTime;

    /// 买家标识(OPEN_ID)
    private String buyerId;

    /// 通道支付流水号(T_PAY_NO)
    private String outTransNo;

    /// 星驿付应答串(处理成功/验签失败的 JSON, 主应用原样返回)
    private String ackBody;

    /// 原始回调明文(用于对账/排障)
    private String syncData;
}
