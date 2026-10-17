package cn.daxpay.open.channel.jeepay.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

/// # Jeepay通道退款同步响应(主应用侧)
///
/// refundStatus 为 Jeepay 原始状态码("0"订单生成/"1"退款中/"2"成功/"3"失败/"4"关闭),
/// 到平台抽象态的映射由主应用完成。
@Data
public class JeepayRefundSyncResp {

    /// 同步原始数据(JSON 全量, 落同步记录)
    private String syncData;

    /// 商户退款单号(mchRefundNo)
    private String outRefundNo;

    /// Jeepay退款单号(refundOrderId)
    private String tradeNo;

    /// 退款状态(Jeepay 原始码)
    private String refundStatus;

    /// 退款金额(单位: 分, 成功态回填)
    private Long refundAmount;

    /// 完成时间(东八区 OffsetDateTime)
    private OffsetDateTime finishTime;
}
