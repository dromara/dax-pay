package cn.daxpay.open.channel.alipay.client.req;

import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 支付宝通道分账请求(发起/同步共用)
///
/// 与子应用 dax-pay-channel-one 的 `AlipayAllocReq` 镜像, 字段对齐。
/// 对应支付宝接口: 发起 alipay.trade.order.settle / 同步 alipay.trade.order.settle.query。
@Data
@Accessors(chain = true)
public class AlipayAllocReq {

    /// 平台分账单号(发起=allocNo, 同步按此 + tradeNo 反查)
    private String outRequestNo;

    /// 原支付通道订单号(支付宝 trade_no, 分账上送通道用)
    private String tradeNo;

    /// 分账模式(async=异步分账, 发起时必填)
    private String royaltyMode;

    /// 分账子参数列表(发起时必填, 同步可空)
    private List<RoyaltyParam> royaltyParameters;

    /// 通道调用凭证
    private AlipaySdkCredential credential;

    /// 分账子参数(单个接收方)
    @Data
    @Accessors(chain = true)
    public static class RoyaltyParam {

        /// 接收方类型(userId / loginName, 对应支付宝 trans_in_type)
        private String transInType;

        /// 接收方账号(对应支付宝 trans_in)
        private String transIn;

        /// 分账金额(分)
        private Long amount;
    }
}
