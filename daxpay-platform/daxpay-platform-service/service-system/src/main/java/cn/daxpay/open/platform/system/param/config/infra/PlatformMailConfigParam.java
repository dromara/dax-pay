package cn.daxpay.open.platform.system.param.config.infra;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 平台邮件发件箱配置参数
///
@Data
@Accessors(chain = true)
@Schema(title = "平台邮件发件箱配置参数")
public class PlatformMailConfigParam {

    /// SMTP 服务器地址
    @NotBlank(message = "{validation.field.host.notBlank}")
    @Schema(description = "SMTP服务器地址")
    private String host;

    /// SMTP 服务器端口
    @NotNull(message = "{validation.field.port.notNull}")
    @Min(value = 1, message = "{validation.field.port.range}")
    @Max(value = 65535, message = "{validation.field.port.range}")
    @Schema(description = "SMTP服务器端口")
    private Integer port;

    /// 发件邮箱账号
    @NotBlank(message = "{validation.field.username.notBlank}")
    @Schema(description = "发件邮箱账号")
    private String username;

    /// SMTP 授权码/密码(为空表示沿用库中已存授权码)
    @Schema(description = "SMTP授权码")
    private String password;

    /// 发件地址(为空时使用 username)
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
