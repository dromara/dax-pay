package cn.daxpay.open.channel.alipay.client.resp;

import cn.daxpay.open.channel.alipay.client.enums.AlipayPayBodyType;
import lombok.Data;

import java.time.OffsetDateTime;

/// # 支付宝通道支付响应
///
/// 与子应用 dax-pay-channel-one 的 `AlipayPayResp` 镜像, 字段对齐。
@Data
public class AlipayPayResp {

    // ===== 基础交易信息(所有支付方式) =====

    /// 商户订单号(透传 Req.outTradeNo)
    private String outTradeNo;

    /// 支付宝交易号(trade_no)
    private String tradeNo;

    /// 支付内容
    private String payBody;

    /// 支付内容类型
    private AlipayPayBodyType payBodyType;

    // ===== 状态信息 =====

    /// 是否已终态完成(true 表示同步即完成, BARCODE 付款码 code=10000 时为 true)
    private Boolean complete;

    // ===== 时间信息(BARCODE 同步成功时返回) =====

    /// 完成时间(gmt_payment)
    private OffsetDateTime finishTime;

    // ===== 金额信息(BARCODE 同步成功时返回, 单位: 分) =====

    /// 订单总金额(total_amount)
    private Long totalAmount;

    /// 买家实付金额(buyer_pay_amount)
    private Long buyerPayAmount;

    /// 商家实收金额(receipt_amount)
    private Long receiptAmount;

    // ===== 用户信息(BARCODE 同步成功时返回) =====

    /// 买家支付宝用户号(buyer_user_id, 2088开头)
    private String buyerUserId;

    /// 买家支付宝开放ID(buyer_open_id)
    private String buyerOpenId;

    /// 买家登录账号(buyer_logon_id, 手机号/邮箱)
    private String buyerLogonId;
}
