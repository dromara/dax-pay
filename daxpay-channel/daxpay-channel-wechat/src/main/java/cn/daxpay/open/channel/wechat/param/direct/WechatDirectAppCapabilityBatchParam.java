package cn.daxpay.open.channel.wechat.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 微信直连支付能力关联应用批量保存参数
///
/// 全量覆盖某通道商户下「支付能力 → 直连应用」的绑定关系：先清除旧记录，再按 items 批量插入。
/// items 为空表示清空该通道商户下所有绑定。
///
@Data
@Accessors(chain = true)
@Schema(title = "微信直连支付能力关联应用批量保存参数")
public class WechatDirectAppCapabilityBatchParam {

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    @NotBlank(message = "{validation.field.channelMchNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Valid
    @Schema(description = "支付能力关联应用列表")
    private List<WechatDirectAppCapabilityItem> items;
}
