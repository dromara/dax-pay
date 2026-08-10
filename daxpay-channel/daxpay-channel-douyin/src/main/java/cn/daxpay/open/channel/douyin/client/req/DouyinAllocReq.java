package cn.daxpay.open.channel.douyin.client.req;

import cn.daxpay.open.channel.douyin.client.credential.DouyinSdkCredential;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 抖音通道分账请求(发起/同步共用)
///
/// 与子应用 dax-pay-channel-one 的 `DouyinAllocReq` 镜像, 字段对齐。
/// 对应抖音接口: 发起 splitFund / 同步 querySplitFund。
@Data
@Accessors(chain = true)
public class DouyinAllocReq {

    /// 平台分账单号(发起=allocNo, 同步按此反查)
    private String outTradeNo;

    /// 原支付通道订单号(抖音 trade_no, 分账上送通道用)
    private String tradeNo;

    /// 分账接收方列表(发起必填, 同步可空)
    private List<ReceiverInfo> receiverInfoDtos;

    /// 异步通知地址(抖音分账必传)
    private String notifyUrl;

    /// 通道调用凭证
    private DouyinSdkCredential credential;

    /// 分账接收方(单个)
    @Data
    @Accessors(chain = true)
    public static class ReceiverInfo {

        /// 接收方类型(MERCHANT_ID / PERSONAL_OPENID)
        private String type;

        /// 接收方账号(商户号或个人 openid)
        private String account;

        /// 接收方姓名(个人时必填)
        private String name;

        /// 分账金额(分)
        private Long amount;
    }
}
