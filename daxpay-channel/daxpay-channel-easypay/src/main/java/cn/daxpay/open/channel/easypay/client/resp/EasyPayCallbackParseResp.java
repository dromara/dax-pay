package cn.daxpay.open.channel.easypay.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 易支付回调验签解析响应
///
/// 子应用用平台公钥验签通过后, 将标准化业务字段回传主应用。
/// 易支付仅有支付回调(退款为同步接口无通知), tradeType 恒为 PAY。
///
/// tradeStatus 为主应用契约标准状态(SUCCESS/FAIL/PROCESSING),
/// 由子应用将易支付 trade_status 标准化后回传。
@Data
@Accessors(chain = true)
public class EasyPayCallbackParseResp {

    /// 是否验签通过
    private Boolean success;

    /// 回调类型(恒为 PAY)
    private String tradeType;

    /// 商户订单号(out_trade_no = 主应用支付交易号)
    private String outTradeNo;

    /// 易支付交易号(trade_no)
    private String tradeNo;

    /// 交易金额(单位: 分, money 元换算)
    private Long amount;

    /// 交易状态(标准化: TRADE_SUCCESS → SUCCESS, 其他 → FAIL)
    private String tradeStatus;

    /// 完成时间(东八区 OffsetDateTime, endtime 优先, 缺失回退 timestamp 秒级时间戳)
    private OffsetDateTime finishTime;

    /// 买家标识(buyer)
    private String buyerId;
}
