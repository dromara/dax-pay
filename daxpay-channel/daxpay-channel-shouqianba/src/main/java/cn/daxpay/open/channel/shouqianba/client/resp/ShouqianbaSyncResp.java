package cn.daxpay.open.channel.shouqianba.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 收钱吧通道订单同步响应(主应用侧)
///
/// tradeState 为收钱吧原始订单状态(order_status: PAID 已支付 / FAIL 失败 / CANCELED 已取消 /
/// REFUNDED·PARTIAL_REFUNDED 已退款(支付曾成功) / 其他处理中)。
@Data
@Accessors(chain = true)
public class ShouqianbaSyncResp {

    /// 商户订单号(透传 client_sn)
    private String outTradeNo;

    /// 收钱吧流水号(sn)
    private String outOrderNo;

    /// 收钱吧订单状态(order_status 原始码)
    private String tradeState;

    /// 用户实付金额(单位: 分, 成功时返回)
    private Long realAmount;

    /// 完成时间(成功时返回)
    private OffsetDateTime finishTime;

    /// 买家标识(成功时返回 payer_uid)
    private String buyerId;

    /// 通道支付凭证号(成功时返回 trade_no)
    private String outTransNo;

    /// 原始响应(用于对账/排障)
    private String syncData;
}
