package cn.daxpay.open.channel.douyin.client.resp;

import lombok.Data;

/// # 抖音通道转账响应(发起/同步共用)
///
/// 与子应用 dax-pay-channel-one 的 `DouyinTransferResp` 镜像, 字段对齐。
@Data
public class DouyinTransferResp {

    /// 通道转账单号(抖音 transfer_bill_no)
    private String transferBillNo;

    /// 转账状态(同步返回: ACCEPTED/TRANSFERING/SUCCESS/FAIL)
    private String state;

    /// 转账失败原因(同步返回 fail_reason)
    private String failReason;
}
