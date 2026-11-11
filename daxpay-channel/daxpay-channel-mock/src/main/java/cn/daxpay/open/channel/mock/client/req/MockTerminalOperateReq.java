package cn.daxpay.open.channel.mock.client.req;

import cn.daxpay.open.channel.mock.client.credential.MockSdkCredential;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/// # Mock 终端报备操作请求(主应用侧)
///
/// 与 mock 子应用 `channel.req.MockTerminalOperateReq` 镜像, 字段一字不差(人工维护双份)。
/// register/cancel/sync 三端点共用; 注销/同步优先按 outTerminalNo 定位,
/// 未携带时按 channelMchNo + systemTerminalNo + type 三元组定位。
@Data
@Accessors(chain = true)
public class MockTerminalOperateReq {

    /// 通道调用凭证(直连空壳 / 服务商经 MockIsvConfigAssembler 反解)
    private MockSdkCredential credential;

    /// 通道商户号(台账归属与报备幂等三元组成员)
    private String channelMchNo;

    /// 报送类型(common/wechat/alipay/union)
    private String type;

    /// 商户终端号(报备锚点)
    private String systemTerminalNo;

    /// 通道侧终端号(注销/同步定位用; 报备时为空)
    private String outTerminalNo;

    /// 通道差异化报备参数(演练字段, 原样透传子应用留痕)
    private Map<String, Object> extra;
}
