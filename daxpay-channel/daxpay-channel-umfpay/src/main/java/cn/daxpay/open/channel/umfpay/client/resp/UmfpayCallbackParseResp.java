package cn.daxpay.open.channel.umfpay.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 联动优势通道回调验签解析响应(主应用侧)
///
/// 子应用对联动优势异步通知(GET 参数 + RSA 签名)验签解析后回传,
/// [getAckBody] 为 HTML META 应答页, 主应用以 text/html 原样返回给联动优势。
@Data
@Accessors(chain = true)
public class UmfpayCallbackParseResp {

    /// 验签是否通过 + 解析是否成功
    private Boolean success;

    /// 商户侧单号(回调 order_id = 原支付交易号)
    private String outTradeNo;

    /// 原下单日期(回调 mer_date)
    private String merDate;

    /// 交易状态(回调 trade_state 原始码, TRADE_SUCCESS 表示成功)
    private String tradeStatus;

    /// 交易金额(单位: 分, 回调 amount 携带时解析)
    private Long amount;

    /// 通道外部交易号(回调 trade_no)
    private String outTransNo;

    /// 买家标识(回调 mer_cust_id)
    private String buyerId;

    /// HTML META 应答页(主应用以 text/html 原样回给联动优势)
    private String ackBody;

    /// 错误描述(验签失败或解析异常时填充)
    private String errorMsg;

    /// 原始回调参数串(用于对账/排障)
    private String syncData;
}
