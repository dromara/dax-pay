package cn.daxpay.open.payment.douyin.result.platform;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 平台抖音应用授权认证配置
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "平台抖音应用授权认证配置")
public class DyPlatformAppAuthConfigResult extends BaseResult {

    @Schema(description = "平台抖音应用ID")
    private Long dyPlatformAppId;

    @SensitiveInfo(front = 12, end = 12)
    @Schema(description = "应用密钥(已脱敏)")
    private String appSecret;
}
