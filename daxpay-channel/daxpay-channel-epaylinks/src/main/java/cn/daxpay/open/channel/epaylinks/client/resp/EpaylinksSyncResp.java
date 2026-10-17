package cn.daxpay.open.channel.epaylinks.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 易票联通道订单同步响应(主应用侧)
@Data
@Accessors(chain = true)
public class EpaylinksSyncResp {

    /// 商户订单号(透传)
    private String outTradeNo;

    /// 易票联平台交易号(transactionNo)
    private String outOrderNo;

    /// 易票联交易状态(payState 原始码: 00 成功 / 01 失败 / 其他处理中)
    private String tradeState;

    /// 订单金额(单位: 分, 成功时返回)
    private Long realAmount;

    /// 完成时间(成功时返回)
    private OffsetDateTime finishTime;

    /// 买家标识(成功时返回)
    private String buyerId;

    /// 渠道单号(成功时返回 channelOrder)
    private String outTransNo;

    /// 原始响应明文(用于对账/排障)
    private String syncData;
}
