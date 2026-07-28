package cn.daxpay.open.payment.wx.result.platform;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 平台微信应用默认能力绑定
///
/// 含应用名称/AppId/类型等冗余字段(由 Service 填充)。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "平台微信应用默认能力绑定")
public class WxPlatformAppCapabilityResult extends BaseResult {

    @Schema(description = "支付产品编码")
    private String product;

    @Schema(description = "支付能力编码")
    private String capability;

    @Schema(description = "平台微信应用ID")
    private Long wxPlatformAppId;

    @Schema(description = "应用名称(冗余展示)")
    private String appName;

    @Schema(description = "微信应用AppId(冗余展示)")
    private String wxAppId;

    @Schema(description = "应用类型(冗余展示): official_account/mini_program/mobile_app")
    private String appType;
}
