package org.dromara.daxpay.channel.wechat.result.isv;

import org.dromara.daxpay.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 微信服务商应用
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "微信服务商应用")
public class WechatIsvAppResult extends BaseResult {

    @Schema(description = "应用名称")
    private String appName;

    @Schema(description = "应用类型")
    private String appType;

    @Schema(description = "微信应用AppId")
    private String wxAppId;
}
