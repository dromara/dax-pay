package cn.daxpay.open.platform.system.param.mobile;

import cn.daxpay.open.platform.system.mobile.config.param.AlipayMiniAppConfigParam;
import cn.daxpay.open.platform.system.mobile.config.param.DyMiniAppConfigParam;
import cn.daxpay.open.platform.system.mobile.config.param.WxMiniAppConfigParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 移动端应用配置参数
///
/// 保存时按 appType + platform 进行 upsert。平台密钥走嵌套强类型对象, 与 platform 一一对应:
/// - wx_mini → wxMini
/// - alipay_mini → alipayMini
/// - dy_mini → dyMini
@Data
@Accessors(chain = true)
@Schema(title = "移动端应用配置参数")
public class MobileAppParam {

    @Schema(description = "主键(更新时传)")
    private Long id;

    /// 端类型
    /// @see cn.daxpay.open.platform.system.enums.MobileAppTypeEnum
    @NotBlank(message = "{validation.field.appType.notBlank}")
    @Schema(description = "端类型: merchant/admin/cashier")
    private String appType;

    /// 移动平台
    /// @see cn.daxpay.open.platform.system.enums.MobilePlatformEnum
    @NotBlank(message = "{validation.field.platform.notBlank}")
    @Schema(description = "移动平台: wx_mini/alipay_mini/dy_mini/android/ios")
    private String platform;

    @Valid
    @Schema(description = "微信小程序配置(platform=wx_mini 时必填)")
    private WxMiniAppConfigParam wxMini;

    @Valid
    @Schema(description = "支付宝小程序配置(platform=alipay_mini 时必填)")
    private AlipayMiniAppConfigParam alipayMini;

    @Valid
    @Schema(description = "抖音小程序配置(platform=dy_mini 时必填, 预留)")
    private DyMiniAppConfigParam dyMini;

    @Schema(description = "消息通知配置(jsonb 原始JSON文本, 明文非敏感)")
    private String notifyConfig;

    @Schema(description = "是否启用第三方账号用户绑定")
    private Boolean bindingEnabled;

    @Schema(description = "是否启用")
    private Boolean enabled;

    @Schema(description = "备注")
    private String remark;
}
