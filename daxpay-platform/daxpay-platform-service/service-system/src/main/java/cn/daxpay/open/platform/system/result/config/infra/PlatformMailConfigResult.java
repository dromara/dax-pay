package cn.daxpay.open.platform.system.result.config.infra;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 平台邮件发件箱配置
///
@Data
@Accessors(chain = true)
@Schema(title = "平台邮件发件箱配置")
public class PlatformMailConfigResult {

    /// SMTP 服务器地址
    @Schema(description = "SMTP服务器地址")
    private String host;

    /// SMTP 服务器端口
    @Schema(description = "SMTP服务器端口")
    private Integer port;

    /// 发件邮箱账号
    @Schema(description = "发件邮箱账号")
    private String username;

    /// SMTP 授权码(脱敏回显)
    @SensitiveInfo(front = 3, end = 3)
    @Schema(description = "SMTP授权码")
    private String password;

    /// 发件地址
    @Schema(description = "发件地址")
    private String from;

    /// 发件人显示名
    @Schema(description = "发件人显示名")
    private String nickname;

    /// 传输加密方式(none/starttls/ssl)
    @Schema(description = "传输加密方式")
    private String securityType;

    /// 超时时间(秒)
    @Schema(description = "超时时间(秒)")
    private Integer timeout;

    /// 通道总开关
    @Schema(description = "通道总开关")
    private Boolean enabled;
}
