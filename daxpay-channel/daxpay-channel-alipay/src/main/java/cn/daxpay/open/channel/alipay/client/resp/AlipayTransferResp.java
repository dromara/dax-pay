package cn.daxpay.open.channel.alipay.client.resp;

import lombok.Data;

/// # 支付宝通道转账响应(发起/同步共用)
///
/// 与子应用 dax-pay-channel-one 的 `AlipayTransferResp` 镜像, 字段对齐。
@Data
public class AlipayTransferResp {

    /// 通道转账单号(支付宝 order_id)
    private String orderId;

    /// 转账状态(同步返回: SUCCESS/FAIL/DEALING/REFUND)
    private String status;

    /// 转账失败原因
    private String failReason;

    /// 转账完成时间(支付宝 gmt_finish)
    private String finishTime;
}
