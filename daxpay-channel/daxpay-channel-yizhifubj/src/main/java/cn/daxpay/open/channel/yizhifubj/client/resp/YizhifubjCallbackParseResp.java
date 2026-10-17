package cn.daxpay.open.channel.yizhifubj.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 一直付通道回调验签解析响应(主应用侧)
///
/// 子应用对一直付异步通知解密验签后回传; 支付/退款回调共用, 由 [#tradeType] 区分(PAY/REFUND);
/// 一直付通知应答为明文串 SUCCESS/Fail, 由主应用直接返回, 无需子应用组装应答报文。
@Data
@Accessors(chain = true)
public class YizhifubjCallbackParseResp {

    /// 验签是否通过 + 解析是否成功
    private Boolean success;

    /// 回调类型(PAY 支付回调 / REFUND 退款回调)
    private String tradeType;

    /// 商户侧单号(支付回调 = 原支付交易号; 退款回调 = 退款单号)
    private String outTradeNo;

    /// 通道流水号(serialNumber)
    private String outOrderNo;

    /// 交易状态(SUCCESS 成功 / 其他非成功)
    private String tradeStatus;

    /// 金额(单位: 分)
    private Long amount;

    /// 完成时间(completeDateTime)
    private OffsetDateTime finishTime;

    /// 错误描述(验签失败或解析异常时填充)
    private String errorMsg;

    /// 原始回调明文(解密后的 body, 用于对账/排障)
    private String syncData;
}
