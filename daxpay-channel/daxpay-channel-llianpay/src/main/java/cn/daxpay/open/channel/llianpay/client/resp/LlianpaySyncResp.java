package cn.daxpay.open.channel.llianpay.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 连连通道订单同步响应(主应用侧)
///
/// tradeState 为连连原始交易状态(txn_status: SUCCESS 成功 / FAILURE 失败 / CANCEL 已退回 / 其他处理中)。
@Data
@Accessors(chain = true)
public class LlianpaySyncResp {

    /// 商户订单号(透传)
    private String outTradeNo;

    /// 连连平台流水号(platform_txno)
    private String outOrderNo;

    /// 连连交易状态(txn_status 原始码)
    private String tradeState;

    /// 订单金额(单位: 分, 成功时返回)
    private Long realAmount;

    /// 完成时间(success_time, 成功时返回)
    private OffsetDateTime finishTime;

    /// 买家标识(chnl_user_id, 成功时返回)
    private String buyerId;

    /// 渠道交易单号(chnl_txno, 成功时返回)
    private String outTransNo;

    /// 原始响应(用于对账/排障)
    private String syncData;
}
