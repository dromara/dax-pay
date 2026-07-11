package cn.daxpay.open.platform.system.result.mobile;

import cn.daxpay.open.platform.core.result.BaseResult;
import cn.daxpay.open.platform.system.mobile.config.result.AlipayMiniAppConfigResult;
import cn.daxpay.open.platform.system.mobile.config.result.DyMiniAppConfigResult;
import cn.daxpay.open.platform.system.mobile.config.result.WxMiniAppConfigResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 移动端应用配置结果
///
/// 平台密钥以嵌套强类型返回(敏感字段脱敏), 按 platform 仅填充对应嵌套。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "移动端应用配置结果")
public class MobileAppResult extends BaseResult {

    @Schema(description = "端类型")
    private String appType;

    @Schema(description = "移动平台")
    private String platform;

    @Schema(description = "微信小程序配置")
    private WxMiniAppConfigResult wxMini;

    @Schema(description = "支付宝小程序配置")
    private AlipayMiniAppConfigResult alipayMini;

    @Schema(description = "抖音小程序配置")
    private DyMiniAppConfigResult dyMini;

    @Schema(description = "消息通知配置(jsonb 原始JSON文本, 明文非敏感)")
    private String notifyConfig;

    @Schema(description = "是否启用第三方账号用户绑定")
    private Boolean bindingEnabled;

    @Schema(description = "是否启用")
    private Boolean enabled;

    @Schema(description = "备注")
    private String remark;
}
