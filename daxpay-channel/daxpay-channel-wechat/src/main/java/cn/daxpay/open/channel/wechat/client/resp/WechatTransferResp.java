package cn.daxpay.open.channel.wechat.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

/// # 微信通道转账响应(发起/同步共用)
///
/// 与子应用 dax-pay-channel-one 的 `WechatTransferResp` 镜像, 字段对齐。
@Data
public class WechatTransferResp {

    /// 是否已终态完成(仅同步返回)
    private boolean complete;

    /// 通道转账单号(微信 transfer_bill_no)
    private String transferBillNo;

    /// 拉起转账确认参数(微信 package_info, 需商户二次确认时返回)
    private String packageInfo;

    /// 转账状态(同步返回: ACCEPTED/PROCESSING/WAIT_USER_CONFIRM/TRANSFERING/SUCCESS/FAIL/CANCELLED)
    private String state;

    /// 转账完成时间(同步成功/失败时返回)
    private OffsetDateTime finishTime;

    /// 失败原因(同步返回)
    private String failReason;
}
