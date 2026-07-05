package cn.daxpay.open.channel.wechat.result.isv;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 微信服务商通道商户应用支付能力关联
///
/// 展示通道商户(特约商户)下各支付能力绑定的子商户应用信息,含应用名称/AppId/类型等冗余字段(由 Service 填充)。
/// 注:「服务商默认应用」回退信息由前端另行调用全局 wechat_isv_app_capability 接口获取,不在本结果中冗余。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "微信服务商通道商户应用支付能力关联")
public class WechatIsvMchAppCapabilityResult extends MchBaseResult {

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "支付能力编码")
    private String capability;

    @Schema(description = "关联微信服务商通道商户应用ID")
    private Long wechatIsvMchAppId;

    @Schema(description = "应用名称(冗余展示)")
    private String appName;

    @Schema(description = "微信应用AppId(冗余展示)")
    private String wxAppId;

    @Schema(description = "应用类型(冗余展示): official_account/mini_program/mobile_app")
    private String appType;
}
