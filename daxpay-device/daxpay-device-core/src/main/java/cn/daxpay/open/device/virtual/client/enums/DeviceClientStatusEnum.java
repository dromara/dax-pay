package cn.daxpay.open.device.virtual.client.enums;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 虚拟设备客户端状态
///
/// 由 ack 响应下发, 终端据此校准本地凭证与连接:
/// ACTIVE 在用 / DISABLED 被运营停用 / UNBOUND 已解绑 / REVOKED 凭证失效(验签失败/凭证被清/设备删除)。
/// 心跳与回执共用 ack 通道(APP 约 5 分钟一跳): 解绑会清空 secret, 删除后设备行不可见,
/// 旧凭证下一次心跳即落到 REVOKED, 清凭证回激活页;
/// 时间窗超限与 nonce 重复(重放)走抛错拒绝, 不返回 REVOKED —— 防网络重发/时钟漂移误踢真设备, 心跳下轮自愈。
@Getter
@RequiredArgsConstructor
public enum DeviceClientStatusEnum implements I18nSupport {

    /// 在用
    ACTIVE("active"),

    /// 已停用
    DISABLED("disabled"),

    /// 已解绑
    UNBOUND("unbound"),

    /// 凭证失效
    REVOKED("revoked");

    /// 编码
    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.device_client_status";
    }
}
