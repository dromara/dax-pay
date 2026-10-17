package cn.daxpay.open.channel.youzan.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 有赞通道回调验签解析响应
///
/// 子应用对有赞异步通知(JSON body + 请求头验签材料)验签解析后回传给主应用。
/// 支付回调与退款回调共用本对象, 由 [#tradeType] 区分(PAY/REFUND), 字段按场景取值。
@Data
@Accessors(chain = true)
public class YouzanCallbackParseResp {

    /// 验签是否通过 + 解析是否成功
    private Boolean success;

    /// 回调类型(PAY 支付回调 / REFUND 退款回调)
    private String tradeType;

    /// 商户侧单号(回调 outer_trade_no = 平台 tradeNo / 退款回调为退款关联号, 以实际报文为准)
    private String outTradeNo;

    /// 有赞订单号(回调 trade_no)
    private String outOrderNo;

    /// 交易状态(回调 status, SUCCESS 为成功)
    private String tradeStatus;

    /// 金额(单位: 分, 支付回调 pay_amount; 参考实现未消费该字段, 防御性兼容多形态)
    private Long amount;

    /// 完成时间(回调时间字段, 参考实现未消费, 防御性兼容多形态)
    private OffsetDateTime finishTime;

    /// 外部渠道支付单号(回调 external_payment_no, 微信/支付宝侧单号)
    private String outTransNo;

    /// 错误描述(验签失败或解析异常时填充)
    private String errorMsg;

    /// 原始回调明文(用于对账/排障)
    private String syncData;
}
