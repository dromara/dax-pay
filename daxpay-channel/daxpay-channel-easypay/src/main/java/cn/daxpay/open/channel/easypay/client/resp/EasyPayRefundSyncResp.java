package cn.daxpay.open.channel.easypay.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

/// # 易支付通道退款同步响应
///
/// 子应用查询易支付退款状态后回传给主应用。
/// refundStatus 为易支付原始状态码("1"成功/"0"失败/其他进行中),
/// 到平台抽象态的映射由主应用完成。
@Data
public class EasyPayRefundSyncResp {

    /// 同步原始数据(JSON 全量, 落同步记录)
    private String syncData;

    /// 商户退款单号
    private String outRefundNo;

    /// 易支付退款单号(refund_no)
    private String tradeNo;

    /// 退款状态(易支付原始码: 1=成功 0=失败 其他=进行中)
    private String refundStatus;

    /// 退款金额(单位: 分, 成功态回填)
    private Long refundAmount;

    /// 完成时间(东八区 OffsetDateTime, addtime)
    private OffsetDateTime finishTime;
}
