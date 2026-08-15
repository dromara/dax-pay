package cn.daxpay.open.channel.wechat.client.req;

import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 微信通道分账接收方绑定请求(绑定/解绑共用)
///
/// 与子应用 dax-pay-channel-one 的 `WechatAllocReceiverReq` 镜像, 字段对齐。
/// 对应微信 V3 API: 绑定 profitsharing/receivers/add / 解绑 profitsharing/receivers/delete。
/// 直连与服务商身份由 [credential] 区分(服务商凭证含 subMchId/subAppId)。
@Data
@Accessors(chain = true)
public class WechatAllocReceiverReq {

    /// 接收方类型(MERCHANT_ID / PERSONAL_OPENID / PERSONAL_SUB_OPENID[服务商])
    private String receiverType;

    /// 接收方账号(商户号或 openid; openid 须与凭证 appid/subAppid 维度一致)
    private String receiverAccount;

    /// 接收方名称(MERCHANT_ID 时必填商户全称)
    private String receiverName;

    /// 分账关系类型(平台小写, 子应用转微信原生大写)
    private String relationType;

    /// 自定义分账关系名(relationType=CUSTOM 时必填)
    private String customRelation;

    /// 通道调用凭证
    private WechatSdkCredential credential;
}
