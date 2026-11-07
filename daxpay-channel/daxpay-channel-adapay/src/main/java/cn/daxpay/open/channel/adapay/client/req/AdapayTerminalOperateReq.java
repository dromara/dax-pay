package cn.daxpay.open.channel.adapay.client.req;

import cn.daxpay.open.channel.adapay.client.credential.AdapaySdkCredential;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/// # Adapay 终端报备操作请求(主应用侧)
///
/// 与子应用 dax-pay-channel-two 的 `AdapayTerminalOperateReq` 镜像, 字段对齐。
/// 汇付以 device.add / device.cancel 功能码报备, 成功回传 device_id(通道侧终端号)。
@Data
@Accessors(chain = true)
public class AdapayTerminalOperateReq {

    /// 通道调用凭证
    private AdapaySdkCredential credential;

    /// 报送类型(wechat/alipay 等, 子应用映射汇付 pay_way)
    private String type;

    /// 通道侧终端号(注销时上送 device_id)
    private String outTerminalNo;

    /// 通道差异化报备参数(汇付 device.add 业务字段, 键=汇付参数名)
    private Map<String, Object> extra;
}
