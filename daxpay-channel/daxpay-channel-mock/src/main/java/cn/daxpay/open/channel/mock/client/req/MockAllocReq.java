package cn.daxpay.open.channel.mock.client.req;

import cn.daxpay.open.channel.mock.client.credential.MockSdkCredential;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # Mock 通道分账请求
///
/// 与通道子应用 one 侧 `cn.daxpay.open.channel.mock.req.MockAllocReq` 镜像, 字段对齐;
/// 金额单位恒为分(Long)。
@Data
@Accessors(chain = true)
public class MockAllocReq {

    /// 分账单号(平台分账单号 allocNo 透传, mock 侧幂等主键)
    private String allocNo;

    /// 原支付单商户订单号(mock 通道侧交易号, 留痕关联原支付单)
    private String outTradeNo;

    /// 分账接收方列表(至少一个)
    private List<Receiver> receivers;

    /// 通道调用凭证(占位空壳)
    private MockSdkCredential credential;

    /// 分账接收方(单个)
    @Data
    @Accessors(chain = true)
    public static class Receiver {

        /// 接收方账号
        private String account;

        /// 接收方名称
        private String name;

        /// 分账金额(分)
        private Long amount;
    }
}
