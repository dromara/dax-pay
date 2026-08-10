package cn.daxpay.open.channel.wechat.client.req;

import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 微信通道分账请求(发起/同步共用)
///
/// 与子应用 dax-pay-channel-one 的 `WechatAllocReq` 镜像, 字段对齐。
/// 对应微信 V3 API: 发起 profitsharing/orders / 同步 profitsharing/orders/{out_order_no}。
@Data
@Accessors(chain = true)
public class WechatAllocReq {

    /// 平台分账单号(发起=allocNo 作为微信 out_order_no, 同步按此反查)
    private String outOrderNo;

    /// 原支付通道交易号(微信 transaction_id, 分账上送通道用)
    private String transactionId;

    /// 分账接收方列表(发起必填, 同步可空)
    private List<Receiver> receivers;

    /// 通道调用凭证
    private WechatSdkCredential credential;

    /// 分账接收方(单个)
    @Data
    @Accessors(chain = true)
    public static class Receiver {

        /// 接收方类型(MERCHANT_ID / PERSONAL_OPENID)
        private String type;

        /// 接收方账号(商户号或个人 openid)
        private String account;

        /// 接收方姓名(个人 openid 时需填, 商户号可空)
        private String name;

        /// 分账金额(分)
        private Long amount;

        /// 分账描述(对应微信 description, 默认"订单分账")
        private String description;
    }
}
