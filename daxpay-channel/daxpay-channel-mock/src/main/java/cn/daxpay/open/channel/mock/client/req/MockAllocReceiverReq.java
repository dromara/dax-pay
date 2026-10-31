package cn.daxpay.open.channel.mock.client.req;

import cn.daxpay.open.channel.mock.client.credential.MockSdkCredential;
import lombok.Data;
import lombok.experimental.Accessors;

/// # Mock 通道分账接收方绑定/解绑请求
///
/// 与通道子应用 one 侧 `cn.daxpay.open.channel.mock.req.MockAllocReceiverReq` 镜像, 字段对齐。
/// mock 零资料接入, 绑定/解绑恒成功仅留痕。
@Data
@Accessors(chain = true)
public class MockAllocReceiverReq {

    /// 接收方账号
    private String account;

    /// 接收方名称(可空)
    private String name;

    /// 通道调用凭证(占位空壳)
    private MockSdkCredential credential;
}
