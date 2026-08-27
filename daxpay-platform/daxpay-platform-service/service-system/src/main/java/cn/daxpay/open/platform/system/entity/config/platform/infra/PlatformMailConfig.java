package cn.daxpay.open.platform.system.entity.config.platform.infra;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Objects;

/// # 平台邮件发件箱配置
///
/// SMTP 发件服务器配置, 供站内通知邮件外发使用, 数据加密存储
@Data
@Accessors(chain = true)
public class PlatformMailConfig {

    /// SMTP 服务器地址
    private String host;

    /// SMTP 服务器端口(SSL 常用 465, STARTTLS 常用 587, 明文 25)
    private Integer port;

    /// 发件邮箱账号(登录 SMTP 的用户名, 通常即发件邮箱地址)
    private String username;

    /// SMTP 授权码/密码(敏感, 加密存储)
    private String password;

    /// 发件地址(为空时使用 username 作为发件地址)
    private String from;

    /// 发件人显示名(如 "DaxPay 平台")
    private String nickname;

    /// 传输加密方式(none明文/starttls/ssl)
    private String securityType;

    /// 连接与读取超时时间(秒)
    private Integer timeout;

    /// 邮件通道总开关(关闭后所有业务邮件发送直接跳过)
    private Boolean enabled;

    /// 传输加密方式, 默认 SSL
    public String getSecurityType() {
        return Objects.toString(securityType, "ssl");
    }

    /// 超时时间, 默认 10 秒
    public Integer getTimeout() {
        return timeout == null ? 10 : timeout;
    }

    /// 通道开关, 默认关闭
    public Boolean getEnabled() {
        return Objects.equals(enabled, Boolean.TRUE);
    }
}
