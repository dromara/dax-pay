package cn.daxpay.open.payment.douyin.param.platform;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 平台抖音应用授权认证配置保存参数
///
/// appSecret 为空表示不更新(由 Service 处理)。
///
@Data
@Accessors(chain = true)
@Schema(title = "平台抖音应用授权认证配置保存参数")
public class DyPlatformAppAuthConfigParam {

    @Schema(description = "主键,新增时不传")
    private Long id;

    @NotNull(message = "{validation.field.dyPlatformAppId.notNull}")
    @Schema(description = "平台抖音应用ID")
    private Long dyPlatformAppId;

    @Schema(description = "应用密钥(加密存储)，为空表示不更新")
    private String appSecret;
}
