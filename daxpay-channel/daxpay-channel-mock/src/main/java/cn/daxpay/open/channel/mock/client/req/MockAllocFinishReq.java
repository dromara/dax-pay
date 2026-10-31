package cn.daxpay.open.channel.mock.client.req;

import cn.daxpay.open.channel.mock.client.credential.MockSdkCredential;
import lombok.Data;
import lombok.experimental.Accessors;

/// # Mock 通道分账完结请求
///
/// 与通道子应用 one 侧 `cn.daxpay.open.channel.mock.req.MockAllocFinishReq` 镜像, 字段对齐。
/// 完结 = 解冻剩余冻结资金给商户本身, 无接收方明细。
@Data
@Accessors(chain = true)
public class MockAllocFinishReq {

    /// 分账单号(完结单自身的平台分账单号, mock 侧不存在时按空明细完结单建单)
    private String allocNo;

    /// 原支付单商户订单号(mock 通道侧交易号, mock 侧场景规则金额个位依据)
    private String outTradeNo;

    /// 通道调用凭证(占位空壳)
    private MockSdkCredential credential;
}
