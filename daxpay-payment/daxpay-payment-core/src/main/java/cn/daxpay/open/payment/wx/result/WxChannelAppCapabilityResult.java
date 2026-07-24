package cn.daxpay.open.payment.wx.result;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 通道商户微信应用能力绑定
///
/// 含应用名称/AppId/类型等冗余字段(由 Service 填充)。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "通道商户微信应用能力绑定")
public class WxChannelAppCapabilityResult extends MchBaseResult {

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "支付能力编码")
    private String capability;

    @Schema(description = "应用档位：platform/merchant")
    private String appScope;

    @Schema(description = "微信应用主数据主键")
    private Long wxAppRefId;

    @Schema(description = "应用名称(冗余展示)")
    private String appName;

    @Schema(description = "微信应用AppId(冗余展示)")
    private String wxAppId;

    @Schema(description = "应用类型(冗余展示): official_account/mini_program/mobile_app")
    private String appType;
}
