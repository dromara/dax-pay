package cn.daxpay.open.channel.kuaiqian.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 快钱通道订单同步响应(主应用侧)
///
/// tradeState 为快钱原始交易状态(txnStatus: S 成功 / F 失败 / R 拒绝 / 其他处理中)。
@Data
@Accessors(chain = true)
public class KuaiqianSyncResp {

    /// 商户订单号(透传)
    private String outTradeNo;

    /// 快钱订单控制号(idOrderCtrl)
    private String outOrderNo;

    /// 快钱交易状态(txnStatus 原始码)
    private String tradeState;

    /// 用户实付金额(单位: 分, 成功时返回)
    private Long realAmount;

    /// 完成时间(成功时返回)
    private OffsetDateTime finishTime;

    /// 买家标识(成功时返回 thirdPartyBuyerId)
    private String buyerId;

    /// 通道外部交易号(channelTradeNo, 成功时返回)
    private String outTransNo;

    /// 原始响应明文(用于对账/排障)
    private String syncData;
}
