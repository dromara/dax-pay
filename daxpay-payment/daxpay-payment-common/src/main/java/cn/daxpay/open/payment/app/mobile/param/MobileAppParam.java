package cn.daxpay.open.payment.app.mobile.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 移动端应用配置参数
///
/// 保存时按 appType + platform 进行 upsert, 二者组合为唯一键。
@Data
@Accessors(chain = true)
@Schema(title = "移动端应用配置参数")
public class MobileAppParam {

    @Schema(description = "主键(更新时必填)")
    private Long id;

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
