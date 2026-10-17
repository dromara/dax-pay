package cn.daxpay.open.channel.jlpay.client.resp;

import cn.daxpay.open.channel.jlpay.client.enums.JlpayPayBodyType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 嘉联通道支付响应
///
/// 子应用调用嘉联 OpenAPI 后回传给主应用。
/// - QRCODE(主扫): 异步支付(complete=false), 返回聚合二维码 code_url, 等待异步通知/查单确认
/// - MICROPAY(被扫): 同步返回扣款结果, status=2 即成功(complete=true)
@Data
@Accessors(chain = true)
public class JlpayPayResp {

    /// 商户订单号(透传 JlpayPayReq.outTradeNo)
    private String outTradeNo;

    /// 嘉联交易号(transaction_id, 供后续查单/退款/撤销)
    private String outOrderNo;

    /// 支付内容(主扫返回二维码链接 code_url; 被扫无支付内容)
    private String payBody;

    /// 支付内容类型(恒 QR_CODE, 被扫为空)
    private JlpayPayBodyType payBodyType;

    /// 是否已终态完成(主扫恒 false; 被扫按同步扣款结果)
    private Boolean complete;

    /// 用户实付金额(单位: 分, 被扫同步成功时返回 total_fee)
    private Long realAmount;

    /// 完成时间(被扫同步成功时返回)
    private OffsetDateTime finishTime;

    /// 买家标识(被扫同步成功时返回 sub_openid)
    private String buyerId;

    /// 渠道方交易单号(chn_transaction_id, 成功时返回)
    private String outTransNo;

    /// 原始响应明文(用于对账/排障)
    private String syncData;
}
