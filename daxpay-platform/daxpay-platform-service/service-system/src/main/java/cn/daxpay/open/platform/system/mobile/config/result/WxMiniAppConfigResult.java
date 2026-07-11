package cn.daxpay.open.platform.system.mobile.config.result;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 微信小程序应用配置出参(敏感字段脱敏)
@Data
@Accessors(chain = true)
@Schema(title = "微信小程序应用配置")
public class WxMiniAppConfigResult {

    @Schema(description = "小程序 AppId")
    private String appId;

    @SensitiveInfo
    @Schema(description = "小程序 AppSecret")
    private String appSecret;

    @Schema(description = "原始 ID")
    private String originalId;
}
