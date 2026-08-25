package cn.daxpay.open.platform.system.entity.config.platform.security;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 通行密钥(WebAuthn)配置
///
/// 对应 system_platform_config 表 config_type=security_webauthn。
/// rpId 为 WebAuthn 依赖方域名(如 example.com), 一旦变更已注册的通行密钥将全部失效;
/// origins 为允许的调用来源完整地址列表(协议+域名+端口, 如 https://admin.example.com)。
///
@Data
@Accessors(chain = true)
public class PlatformWebAuthnConfig {

    /// 默认依赖方显示名称
    public static final String DEFAULT_RP_NAME = "DaxPay";

    /// 是否启用通行密钥认证
    private Boolean enabled;
    /// 依赖方ID(域名, 如 localhost 或 admin.example.com)
    private String rpId;
    /// 依赖方显示名称(认证器弹窗展示)
    private String rpName;
    /// 允许的调用来源列表(完整 origin, 含协议域名端口)
    private List<String> origins;
}
