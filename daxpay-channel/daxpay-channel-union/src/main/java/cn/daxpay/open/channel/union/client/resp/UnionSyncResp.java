package cn.daxpay.open.channel.union.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 云闪付通道支付同步响应
@Data
@Accessors(chain = true)
public class UnionSyncResp {

    /// 商户订单号(回显)
    private String outTradeNo;

    /// 统一交易状态(SUCCESS / PROGRESS / CLOSED)
    private String tradeStatus;

    /// 订单金额(单位: 分)
    private Long totalAmount;

    /// 实付金额(单位: 分)
    private Long realAmount;

    /// 支付成功时间(yyyyMMddHHmmss, 东八区)
    private String payTime;

    /// 银联交易查询凭证(退款时作为 origQryId)
    private String queryId;

    /// 买家标识
    private String buyerId;

    /// 查询失败时的错误信息
    private String errorMsg;
}
