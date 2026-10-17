package cn.daxpay.open.channel.helipay.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 合利宝通道订单同步响应(子应用 → 主应用)
///
/// tradeState 为合利宝原始订单状态码(rt7_orderStatus, SUCCESS=成功),
/// 到平台抽象态的映射由主应用完成。
@Data
@Accessors(chain = true)
public class HelipaySyncResp {

    /// 同步原始数据(JSON 全量, 落同步记录)
    private String syncData;

    /// 商户订单号(P2_orderId 原样回显)
    private String outTradeNo;

    /// 合利宝流水号(rt6_serialNumber)
    private String outOrderNo;

    /// 渠道外部交易单号(rt18_outTransactionOrderId, 微信/支付宝侧单号)
    private String outTransNo;

    /// 订单状态(rt7_orderStatus 原始码, SUCCESS=成功)
    private String tradeState;

    /// 订单金额(单位: 分, 成功态回填)
    private Long totalAmount;

    /// 完成时间(东八区 OffsetDateTime)
    private OffsetDateTime finishTime;

    /// 买家标识(rt11_openId)
    private String openId;
}
