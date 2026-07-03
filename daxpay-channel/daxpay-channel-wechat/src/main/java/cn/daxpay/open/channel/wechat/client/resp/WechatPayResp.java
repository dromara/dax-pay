package cn.daxpay.open.channel.wechat.client.resp;

import cn.daxpay.open.channel.wechat.client.enums.WechatPayBodyType;
import lombok.Data;

import java.time.OffsetDateTime;

/// # 微信通道支付响应
///
/// 与子应用 dax-pay-channel-one 的 `WechatPayResp` 镜像, 字段对齐。
@Data
public class WechatPayResp {
    /// 商户订单号(透传 WechatPayReq.outTradeNo)
    private String outTradeNo;
    /// 微信支付订单号(transaction_id)
    private String transactionId;
    /// 支付内容(H5 跳转链接 / 二维码内容 / 调起参数 JSON)
    private String payBody;
    /// 支付内容类型
    private WechatPayBodyType payBodyType;
    /// 是否已终态完成(MICROPAY 付款码同步成功时为 true)
    private Boolean complete;
    /// 完成时间
    private OffsetDateTime finishTime;
    /// 订单总金额(单位: 分)
    private Long totalAmount;
    /// 用户支付金额(单位: 分)
    private Long payerTotal;
    /// 用户标识(openid)
    private String openId;
}
