package cn.daxpay.open.payment.douyin.result.platform;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 平台抖音应用默认能力绑定
///
/// 含应用名称/AppId/类型等冗余字段(由 Service 填充)。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "平台抖音应用默认能力绑定")
public class DyPlatformAppCapabilityResult extends BaseResult {

    @Schema(description = "支付产品编码")
    private String product;

    @Schema(description = "支付能力编码")
    private String capability;

    @Schema(description = "平台抖音应用ID")
    private Long dyPlatformAppId;

    @Schema(description = "应用名称(冗余展示)")
    private String appName;

    @Schema(description = "抖音应用AppId(冗余展示)")
    private String douyinAppId;

    @Schema(description = "应用类型(冗余展示): mini_program/mobile_app/web_app")
    private String appType;
}
