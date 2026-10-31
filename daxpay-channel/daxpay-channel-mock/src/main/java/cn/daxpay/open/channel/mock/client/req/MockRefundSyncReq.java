package cn.daxpay.open.channel.mock.client.req;

import cn.daxpay.open.channel.mock.client.credential.MockSdkCredential;
import lombok.Data;

/// # Mock 通道退款同步请求
///
/// 与通道子应用 one 侧 `cn.daxpay.open.channel.mock.req.MockRefundSyncReq` 镜像, 字段对齐。
@Data
public class MockRefundSyncReq {

    /// 商户退款号(透传 MockRefundReq.outRefundNo)
    private String outRefundNo;

    /// 通道调用凭证(占位空壳)
    private MockSdkCredential credential;
}
