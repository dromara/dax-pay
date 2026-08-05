package cn.daxpay.open.channel.adapay.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # Adapay 通道支付同步响应(主应用侧)
@Data
@Accessors(chain = true)
public class AdapaySyncResp {

    /// 商户订单号(回显)
    private String outTradeNo;

    /// Adapay 支付对象 ID(通道订单号, 后续关单/退款的关键凭证)
    private String paymentId;

    /// 统一交易状态(SUCCESS / PROGRESS / CLOSED)
    private String tradeStatus;

    /// 订单金额(单位: 分)
    private Long totalAmount;

    /// 实付金额(单位: 分)
    private Long realAmount;

    /// 支付成功时间
    private String payTime;

    /// 买家标识
    private String buyerId;

    /// 通道流水号(汇付 hf_seq_id)
    private String transOrderNo;

    /// 通道原始返回 JSON 快照(用于同步流水记录)
    private String syncData;

    /// 查询失败时的错误信息
    private String errorMsg;
}
