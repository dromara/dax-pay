package cn.daxpay.open.channel.hkrt.client.req;

import cn.daxpay.open.channel.hkrt.client.credential.HkrtSdkCredential;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/// # 海科融通终端报备操作请求(主应用侧)
///
/// 与子应用 dax-pay-channel-two 的 `HkrtTerminalOperateReq` 镜像, 字段对齐。
/// 海科以 SN(系统终端号)为绑定锚点, 报备成功回传 pn(通道侧终端号)。
@Data
@Accessors(chain = true)
public class HkrtTerminalOperateReq {

    /// 通道调用凭证
    private HkrtSdkCredential credential;

    /// 系统终端号(作为海科侧 sn 上送)
    private String systemTerminalNo;

    /// 通道差异化报备参数(海科: storeId/code/address)
    private Map<String, Object> extra;
}
