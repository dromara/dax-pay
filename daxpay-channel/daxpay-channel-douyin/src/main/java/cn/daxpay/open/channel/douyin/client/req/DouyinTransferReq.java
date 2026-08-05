package cn.daxpay.open.channel.douyin.client.req;

import cn.daxpay.open.channel.douyin.client.credential.DouyinSdkCredential;
import lombok.Data;

/// # 抖音通道转账请求(发起/同步共用)
///
/// 与子应用 dax-pay-channel-one 的 `DouyinTransferReq` 镜像, 字段对齐。
@Data
public class DouyinTransferReq {

    /// 商户转账单号(发起=平台转账单号 transferNo, 同步=通道转账单号 outTransferNo)
    private String outBillNo;

    /// 平台转账单号(同步反查备用)
    private String transferNo;

    /// 转账金额(分, 同步可空)
    private Long amount;

    /// 收款人 openid
    private String openid;

    /// 转账场景ID(transfer_scene_id, 来自通道商户配置)
    private String scene;

    /// 收款人姓名(金额>=2000元必填, 子应用加密上送)
    private String userName;

    /// 转账备注(对应 transfer_remark)
    private String remark;

    /// 收款感知文案(对应 user_recv_perception)
    private String perception;

    /// 异步通知地址(抖音→平台)
    private String notifyUrl;

    /// 通道调用凭证
    private DouyinSdkCredential credential;
}
