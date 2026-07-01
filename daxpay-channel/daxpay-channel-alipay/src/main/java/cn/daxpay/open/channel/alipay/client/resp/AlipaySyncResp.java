package cn.daxpay.open.channel.alipay.client.resp;

import cn.daxpay.open.channel.alipay.service.payment.sync.AlipaySyncService;
import lombok.Data;

import java.time.OffsetDateTime;

/// # 支付宝通道同步响应
///
/// 与子应用 dax-pay-channel-one 的 `AlipaySyncResp` 镜像, 字段对齐。
/// 子应用原样回传支付宝 trade.query 字段, trade_status 映射由主应用 [AlipaySyncService] 完成。
@Data
public class AlipaySyncResp {

    /// 交易状态(WAIT_BUYER_PAY / TRADE_SUCCESS / TRADE_FINISHED / TRADE_CLOSED)
    private String tradeStatus;

    /// 网关返回码(code, 10000=成功)
    private String code;

    /// 业务返回码(sub_code, 如 ACQ.TRADE_NOT_EXIST)
    private String subCode;

    /// 业务返回消息(sub_msg)
    private String subMsg;

    /// 支付宝交易号(trade_no)
    private String tradeNo;

    /// 商户订单号(out_trade_no, 透传请求)
    private String outTradeNo;

    /// 交易付款时间(send_pay_date)
    private OffsetDateTime sendPayDate;

    /// 买家支付宝用户ID(buyer_user_id)
    private String buyerUserId;

    /// 买家支付宝开放ID(buyer_open_id)
    private String buyerOpenId;

    /// 买家实付金额(buyer_pay_amount, 单位: 分)
    private Long buyerPayAmount;
}
