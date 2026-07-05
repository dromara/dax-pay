package cn.daxpay.open.channel.ums.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 银联商务通道支付同步响应
///
/// 统一状态码: SUCCESS / PROGRESS / CLOSED
@Data
@Accessors(chain = true)
public class UmsSyncResp {

    /// 商户订单号(回显)
    private String outTradeNo;

    /// 统一交易状态(SUCCESS / PROGRESS / CLOSED)
    private String tradeStatus;

    /// 订单金额(单位: 分)
    private Long totalAmount;

    /// 实付金额(单位: 分)
    private Long realAmount;

    /// 支付成功时间(yyyy-MM-dd HH:mm:ss)
    private String payTime;

    /// 买家标识
    private String buyerId;

    /// 支付厂商(Alipay / WXPay / UnionPay)
    private String targetSys;

    /// 第三方订单号
    private String targetOrderId;

    /// 查询失败时的错误信息
    private String errorMsg;
}
