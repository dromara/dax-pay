package cn.daxpay.open.payment.wx.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 通道商户微信应用能力绑定批量保存参数
///
/// 按通道商户全量覆盖绑定：先清后插；items 为空表示清空该通道商户下全部绑定。
/// mchNo 由运营端请求体传入；商户端不传，由登录上下文注入。
///
@Data
@Accessors(chain = true)
@Schema(title = "通道商户微信应用能力绑定批量保存参数")
public class WxChannelAppCapabilityBatchParam {

    // 商户号（运营端必传；商户端由上下文注入，故不加 @NotBlank）
    @Schema(description = "商户号")
    private String mchNo;

    // 通道商户号
    @NotBlank(message = "{validation.field.channelMchNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Valid
    @Schema(description = "能力绑定列表")
    private List<WxChannelAppCapabilityParam> items;
}
