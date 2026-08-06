package cn.daxpay.open.channel.wechat.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 微信转账回调验签解析响应(子应用 → 主应用)
///
/// 与子应用 dax-pay-channel-one 的 `WechatTransferCallbackParseResp` 镜像。
/// 仅承载转账回调的结构化业务数据, 与支付/退款回调响应 [WechatCallbackParseResp] 解耦。
@Data
@Accessors(chain = true)
public class WechatTransferCallbackParseResp {

    /// 商户转账单号(转账回调: out_bill_no = 平台转账单号 transferNo)
    private String outBillNo;

    /// 微信转账单号(转账回调: transfer_bill_no = 通道转账单号 outTransferNo)
    private String transferBillNo;

    /// 转账状态(转账回调: SUCCESS/FAIL/CANCELLED/ACCEPTED/PROCESSING/WAIT_USER_CONFIRM/TRANSFERING/CANCELING)
    private String transferState;

    /// 转账失败原因(转账回调: fail_reason)
    private String failReason;

    /// 转账状态更新时间(转账回调: update_time, RFC3339 东八区)
    private String updateTime;

    /// 验签是否通过
    private boolean verified;
}
