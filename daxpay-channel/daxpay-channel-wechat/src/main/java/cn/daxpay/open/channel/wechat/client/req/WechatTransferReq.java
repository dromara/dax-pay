package cn.daxpay.open.channel.wechat.client.req;

import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.payment.trade.transfer.param.TransferReportInfo;
import lombok.Data;

import java.util.List;

/// # 微信通道转账请求(发起/同步共用)
///
/// 与子应用 dax-pay-channel-one 的 `WechatTransferReq` 镜像, 字段对齐。
/// 发起时填写全部字段; 同步仅需 outBillNo(outTransferNo)/transferNo 与 credential。
@Data
public class WechatTransferReq {

    /// 商户转账批次单号(发起=平台转账单号 transferNo, 同步=通道转账单号 outTransferNo)
    private String outBillNo;

    /// 平台转账单号(同步反查备用, 通道单号缺失时使用)
    private String transferNo;

    /// 转账金额(分, 同步可空)
    private Long amount;

    /// 收款人微信 openid
    private String openid;

    /// 转账场景(transfer_scene, 来自通道商户配置)
    private String scene;

    /// 收款人姓名(金额档位校验用)
    private String userName;

    /// 转账备注(对应微信 transfer_remark)
    private String remark;

    /// 异步通知地址(微信→平台)
    private String notifyUrl;

    /// 转账场景报备信息(发起时必填, 同步可空)
    private List<TransferReportInfo> reportInfos;

    /// 通道调用凭证
    private WechatSdkCredential credential;
}
