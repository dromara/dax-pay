package cn.daxpay.open.platform.system.param.protocol;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 协议复制请求参数
///
/// 复制协议到其他端(连同各语言的当前生效版本一起复制)的请求体。
@Data
@Accessors(chain = true)
@Schema(title = "协议复制请求参数")
public class UserProtocolCopyParam {

    /// 源协议ID
    @NotNull(message = "{validation.field.id.notNull}")
    @Schema(description = "源协议ID")
    private Long id;

    /// 目标端类型
    @NotBlank(message = "{validation.field.clientType.notBlank}")
    @Schema(description = "目标端类型")
    private String clientType;
}
