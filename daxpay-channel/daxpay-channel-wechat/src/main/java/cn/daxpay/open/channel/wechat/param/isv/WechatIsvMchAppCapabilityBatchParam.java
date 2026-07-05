package cn.daxpay.open.channel.wechat.param.isv;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 微信服务商通道商户支付能力关联应用批量保存参数
///
/// 全量覆盖某通道商户下「支付能力 → 子商户应用」的绑定关系:先清除旧记录,再按 items 批量插入。
/// items 为空表示清空该通道商户下所有绑定(该能力的应用将自动回退到全局服务商配置)。
/// 仅存子商户显式选择自己应用的记录,选「服务商默认应用」的能力不在此参数中。
///
@Data
@Accessors(chain = true)
@Schema(title = "微信服务商通道商户支付能力关联应用批量保存参数")
public class WechatIsvMchAppCapabilityBatchParam {

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Valid
    @Schema(description = "支付能力关联应用列表(仅含选子商户应用的项,选服务商默认的不传)")
    private List<WechatIsvMchAppCapabilityItem> items;
}
