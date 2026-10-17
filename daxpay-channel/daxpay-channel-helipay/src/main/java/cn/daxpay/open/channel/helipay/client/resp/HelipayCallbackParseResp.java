package cn.daxpay.open.channel.helipay.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 合利宝回调验签解析响应(子应用 → 主应用)
///
/// 子应用用扫码产品签名密钥验签通过后, 将标准化业务字段回传主应用。
/// 合利宝仅接入支付回调(退款为同步结果模式, 无退款通知), tradeType 恒为 PAY。
@Data
@Accessors(chain = true)
public class HelipayCallbackParseResp {

    /// 是否验签通过
    private Boolean success;

    /// 回调类型(恒为 PAY)
    private String tradeType;

    /// 商户订单号(rt2_orderId = 主应用支付交易号)
    private String outTradeNo;

    /// 合利宝系统流水号(rt3_systemSerial)
    private String outOrderNo;

    /// 渠道外部交易单号(rt17_outTransactionOrderId, 微信/支付宝侧单号)
    private String outTransNo;

    /// 交易金额(单位: 分)
    private Long amount;

    /// 交易状态(标准化: SUCCESS / FAIL)
    private String tradeStatus;

    /// 完成时间(东八区 OffsetDateTime)
    private OffsetDateTime finishTime;

    /// 买家标识(rt19_subOpenId)
    private String openId;

    /// 原始回调参数(JSON 序列化, 用于对账/排障)
    private String syncData;
}
