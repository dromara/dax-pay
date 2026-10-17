package cn.daxpay.open.channel.yisheng.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 易生通道订单同步响应
///
/// 子应用调用易生 easypay.merchant.query 查单后回传给主应用,
/// tradeState 为易生原始交易状态(trade_status: SUCCESS 成功, 其他处理中/未支付)。
@Data
@Accessors(chain = true)
public class YishengSyncResp {

    /// 商户订单号(透传)
    private String outTradeNo;

    /// 易生交易号(trade_no)
    private String outOrderNo;

    /// 易生交易状态(trade_status 原始码)
    private String tradeState;

    /// 订单金额(单位: 分, 成功时返回)
    private Long realAmount;

    /// 完成时间(success_time, 成功时返回)
    private OffsetDateTime finishTime;

    /// 买家标识(buyer_id, 成功时返回)
    private String buyerId;

    /// 通道参考号(ref_no, 底层渠道凭证号)
    private String outTransNo;

    /// 原始响应节点明文(用于对账/排障)
    private String syncData;
}
