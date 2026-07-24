package cn.daxpay.open.payment.wx.result;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 平台微信应用
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "平台微信应用")
public class WxPlatformAppResult extends BaseResult {

    @Schema(description = "应用名称")
    private String appName;

    @Schema(description = "应用类型")
    private String appType;

    @Schema(description = "微信应用AppId")
    private String wxAppId;
}
