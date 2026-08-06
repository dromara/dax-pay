package cn.daxpay.open.channel.douyin.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 抖音转账回调验签解析响应(子应用 → 主应用)
///
/// 与子应用 dax-pay-channel-one 的 `DouyinTransferCallbackParseResp` 镜像。
/// 仅承载转账回调的结构化业务数据, 与支付/退款回调响应 [DouyinCallbackParseResp] 解耦。
@Data
@Accessors(chain = true)
public class DouyinTransferCallbackParseResp {

    /// 通道转账单号(转账回调: order_id = 通道转账单号 outTransferNo, 抖音转账通知无商户单号)
    private String transferBillNo;

    /// 转账状态(转账回调: SUCCESS/FAIL/ACCEPTED/TRANSFERING)
    private String transferState;

    /// 转账状态描述(转账回调: status_desc, 含失败原因)
    private String transferStatusDesc;

    /// 成功时间(RFC3339, 转账完成时间)
    private String successTime;

    /// 验签是否通过
    private boolean verified;
}
