package cn.daxpay.open.channel.adapay.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # Adapay 终端报备操作响应(主应用侧)
///
/// 与子应用 dax-pay-channel-two 的 `AdapayTerminalOperateResp` 镜像, 字段对齐。
@Data
@Accessors(chain = true)
public class AdapayTerminalOperateResp {

    /// 通道侧终端号(报备成功回传的汇付 device_id)
    private String deviceNo;
}
