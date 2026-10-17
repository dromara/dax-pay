package cn.daxpay.open.channel.shouqianba.client.credential;

import lombok.Data;

/// # 收钱吧支付 SDK 凭证(主应用侧)
///
/// 主应用从直连密钥配置([cn.daxpay.open.channel.shouqianba.entity.direct.ShouqianbaDirectKeyConfig])提取
/// 终端号与终端密钥后组装, 经声明式 HTTP 客户端下发给子应用 dax-pay-channel-two 发起收钱吧 upay v2 调用。
/// 字段与子应用 cn.daxpay.open.channel.shouqianba.config.ShouqianbaSdkCredential 保持一致(序列化互通)。
@Data
public class ShouqianbaSdkCredential {

    /// 终端号(terminal_sn, 激活后获得)
    private String terminalSn;

    /// 终端密钥(terminal_key, 签名用; 签到重置后需同步更新)
    private String terminalKey;

    /// 操作员(收钱吧要求上送, 缺省 daxpay)
    private String operator;

    /// 是否沙箱环境(收钱吧无独立沙箱网关, 该标识仅透传记录)
    private Boolean sandbox;
}
