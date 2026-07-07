package cn.daxpay.open.channel.hkrt.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

/// # 海科融通回调解析响应(主应用侧, 与子应用镜像)
///
/// 子应用对海科异步通知验签通过后, 将解析出的标准化字段回传主应用。
@Data
public class HkrtCallbackParseResp {

    /// 是否验签通过
    private Boolean success;

    /// 海科交易号(trade_no)
    private String tradeNo;

    /// 商户订单号(out_trade_no, 支付回调)或商户退款单号(退款回调)
    private String outTradeNo;

    /// 银行交易号(bank_trade_no)
    private String transOrderNo;

    /// 交易状态(抽象态 SUCCESS / FAIL, 已屏蔽海科数字码)
    private String tradeStatus;

    /// 金额(单位: 分, order_amount / refund_amount 元转分)
    private Long amount;

    /// 完成时间(东八区 OffsetDateTime)
    private OffsetDateTime finishTime;

    /// 支付类型(扩展字段: WX/ALI/UNIONQR)
    private String payType;

    /// 支付模式(扩展字段: JSAPI/NATIVE)
    private String payMode;

    /// 买家标识(微信 openid / 支付宝 buyer_openid)
    private String buyerId;

    /// 银行卡类型(银联回调时的 card_type)
    private String bankType;
}
