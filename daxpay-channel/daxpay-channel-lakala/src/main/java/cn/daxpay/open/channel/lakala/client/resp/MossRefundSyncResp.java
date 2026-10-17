package cn.daxpay.open.channel.lakala.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

/// # 拉卡拉 MOSS 退款查询响应(主应用侧, 与子应用镜像)
@Data
public class MossRefundSyncResp {

    /// 商户退款单号
    private String outRefundNo;

    /// MOSS 退款交易号(pay_serial)
    private String tradeNo;

    /// 退款状态(SUCCESS / FAIL / 其他=处理中)
    private String refundStatus;

    /// 退款完成时间
    private OffsetDateTime finishTime;

    /// 原始响应数据(解密后业务报文, 用于对账/排障)
    private String syncData;
}
