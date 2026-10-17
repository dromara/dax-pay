package cn.daxpay.open.channel.baofu.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 宝付通道回调验签解析响应(主应用侧)
///
/// 子应用对宝付异步通知(form 提交 dataContent + signStr)验签解析后回传;
/// 宝付聚合线仅支付回调有异步通知(退款为同步终态), tradeType 恒 PAY;
/// 应答串 OK / FAIL 由主应用直接返回(纯文本, 无需子应用组装)。
@Data
@Accessors(chain = true)
public class BaofuCallbackParseResp {

    /// 验签是否通过 + 解析是否成功
    private Boolean success;

    /// 回调类型(恒 PAY)
    private String tradeType;

    /// 商户侧单号(支付回调 = 原支付交易号 outTradeNo)
    private String outTradeNo;

    /// 宝付交易号(tradeNo)
    private String outOrderNo;

    /// 交易状态(txnState: SUCCESS 成功, 其他非成功)
    private String tradeStatus;

    /// 订单金额(txnAmt, 单位: 分; 防御性提取, 供主应用核对)
    private Long amount;

    /// 完成时间(finishTime)
    private OffsetDateTime finishTime;

    /// 买家标识(支付宝 buyer_id)
    private String buyerId;

    /// 渠道外部交易号(微信 transaction_id / 支付宝 trade_no / 云闪付 voucherNum)
    private String outTransNo;

    /// 渠道侧请求流水号(reqChlNo)
    private String reqChlNo;

    /// 错误描述(验签失败或解析异常时填充)
    private String errorMsg;

    /// 原始通知明文(dataContent, 用于对账/排障)
    private String syncData;
}
