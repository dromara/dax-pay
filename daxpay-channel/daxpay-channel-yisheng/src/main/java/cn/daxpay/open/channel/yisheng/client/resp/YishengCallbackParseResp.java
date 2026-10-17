package cn.daxpay.open.channel.yisheng.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 易生通道回调验签解析响应
///
/// 子应用对易生异步通知(form 提交的 biz_content + sign)验签解析后回传给主应用。
/// 易生仅支付回调有异步通知(退款为同步终态), tradeType 恒 PAY。
@Data
@Accessors(chain = true)
public class YishengCallbackParseResp {

    /// 验签是否通过 + 解析是否成功
    private Boolean success;

    /// 回调类型(恒 PAY 支付回调)
    private String tradeType;

    /// 商户侧单号(支付回调 = 原支付交易号 out_trade_no)
    private String outTradeNo;

    /// 易生交易号(trade_no)
    private String outOrderNo;

    /// 交易状态(TRADE_FINISHED 支付成功, 其他非成功)
    private String tradeStatus;

    /// 金额(单位: 分; 防御性提取, 缺省时由主应用按订单金额处理)
    private Long amount;

    /// 完成时间(gmt_payment)
    private OffsetDateTime finishTime;

    /// 买家标识(buyer_id)
    private String buyerId;

    /// 通道参考号(ref_no, 底层渠道凭证号)
    private String outTransNo;

    /// 错误描述(验签失败或解析异常时填充)
    private String errorMsg;

    /// 回调原文(biz_content 明文, 用于对账/排障)
    private String syncData;
}
