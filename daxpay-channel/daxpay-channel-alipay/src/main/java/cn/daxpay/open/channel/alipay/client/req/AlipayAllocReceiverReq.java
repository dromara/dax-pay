package cn.daxpay.open.channel.alipay.client.req;

import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付宝通道分账接收方绑定请求(绑定/解绑共用)
///
/// 与子应用 dax-pay-channel-one 的 `AlipayAllocReceiverReq` 镜像, 字段对齐。
/// 对应支付宝 API: 绑定 alipay.trade.royalty.relation.bind / 解绑 alipay.trade.royalty.relation.unbind。
/// 直连与服务商身份由 [credential] 区分(服务商凭证含 appAuthToken)。
@Data
@Accessors(chain = true)
public class AlipayAllocReceiverReq {

    /// 幂等请求号(绑定记录 id, 支付宝 out_request_no)
    private String outRequestNo;

    /// 接收方类型(USER_ID 用户号 / LOGIN_NAME 登录账号, 子应用映射支付宝原生小写)
    private String receiverType;

    /// 接收方账号(userId 为 2088 开头, loginName 为手机号/邮箱)
    private String receiverAccount;

    /// 接收方名称(可空)
    private String receiverName;

    /// 通道调用凭证
    private AlipaySdkCredential credential;
}
