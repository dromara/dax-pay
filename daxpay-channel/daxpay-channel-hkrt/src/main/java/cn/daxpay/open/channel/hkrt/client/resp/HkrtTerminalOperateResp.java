package cn.daxpay.open.channel.hkrt.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 海科融通终端报备操作响应(主应用侧)
///
/// 与子应用 dax-pay-channel-two 的 `HkrtTerminalOperateResp` 镜像, 字段对齐。
@Data
@Accessors(chain = true)
public class HkrtTerminalOperateResp {

    /// 通道侧终端号(报备成功回传的海科 pn)
    private String deviceNo;
}
