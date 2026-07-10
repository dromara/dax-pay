package cn.daxpay.open.platform.system.result.mobile;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 移动端应用配置结果
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "移动端应用配置结果")
public class MobileAppResult extends BaseResult {

    @Schema(description = "端类型")
    private String appType;

    @Schema(description = "移动平台")
    private String platform;

    @Schema(description = "应用名称")
    private String appName;

    @Schema(description = "平台特有密钥配置(JSON文本)")
    private String appConfig;

    @Schema(description = "消息通知配置(JSON文本)")
    private String notifyConfig;

    @Schema(description = "是否启用第三方账号用户绑定")
    private Boolean bindingEnabled;

    @Schema(description = "是否启用")
    private Boolean enabled;

    @Schema(description = "备注")
    private String remark;
}
