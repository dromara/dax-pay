package cn.daxpay.open.channel.alipay.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 支付宝转账回调验签解析响应(子应用 → 主应用)
///
/// 与子应用 dax-pay-channel-one 的 `AlipayTransferCallbackParseResp` 镜像。
/// 仅承载转账回调的结构化业务数据, 与支付/退款回调响应 [AlipayCallbackParseResp] 解耦。
@Data
@Accessors(chain = true)
public class AlipayTransferCallbackParseResp {

    /// 是否验签通过
    private Boolean success;

    /// 商户转账单号(转账回调: out_biz_no = 平台转账单号 transferNo)
    private String outBizNo;

    /// 支付宝转账单号(转账回调: order_id = 通道转账单号 outTransferNo)
    private String orderId;

    /// 转账状态(转账回调: 原始 status, SUCCESS/FAIL/DEALING/REFUND/CLOSED)
    private String transferStatus;

    /// 转账失败原因(转账回调: sub_msg)
    private String failReason;

    /// 完成时间(转账回调: pay_date, 东八区 OffsetDateTime)
    private OffsetDateTime finishTime;
}
