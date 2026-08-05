package cn.daxpay.open.channel.alipay.client.req;

import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import lombok.Data;

/// # 支付宝通道转账请求(发起/同步共用)
///
/// 与子应用 dax-pay-channel-one 的 `AlipayTransferReq` 镜像, 字段对齐。
@Data
public class AlipayTransferReq {

    /// 商户转账单号(发起=平台转账单号 transferNo, 同步按此反查)
    private String outBizNo;

    /// 转账金额(分, 同步可空)
    private Long amount;

    /// 转账标题(对应 order_title)
    private String title;

    /// 转账备注(对应 remark)
    private String remark;

    /// 收款人账号类型(user_id/open_id/login_name)
    private String payeeType;

    /// 收款人账号
    private String payeeAccount;

    /// 收款人姓名
    private String payeeName;

    /// 通道调用凭证
    private AlipaySdkCredential credential;
}
