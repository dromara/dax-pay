package cn.daxpay.open.channel.yizhifubj.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 一直付通道订单同步响应(主应用侧)
///
/// tradeState 为 5upay 原始交易状态(SUCCESS 成功 / FAILED、CANCEL 失败 / 其他处理中)。
@Data
@Accessors(chain = true)
public class YizhifubjSyncResp {

    /// 商户订单号(透传)
    private String outTradeNo;

    /// 通道流水号(serialNumber)
    private String outOrderNo;

    /// 5upay 交易状态(原始码)
    private String tradeState;

    /// 订单金额(单位: 分, 成功时返回)
    private Long realAmount;

    /// 完成时间(completeDateTime, 成功时返回)
    private OffsetDateTime finishTime;

    /// 原始响应明文(用于对账/排障)
    private String syncData;
}
