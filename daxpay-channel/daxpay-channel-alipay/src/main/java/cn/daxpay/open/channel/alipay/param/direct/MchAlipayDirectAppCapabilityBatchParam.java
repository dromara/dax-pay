package cn.daxpay.open.channel.alipay.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 支付能力关联应用批量保存参数(商户端)
///
/// 商户端专用参数, 不含商户号(防越权: 商户号由控制器从 [cn.daxpay.open.payment.common.context.PaymentContext]
/// 取当前登录商户组装, 对照运营端参数 [AlipayDirectAppCapabilityBatchParam])。
///
/// 全量覆盖某通道商户下「支付能力 → 应用」的绑定关系：先清除旧记录，再按 items 批量插入。
/// items 为空表示清空该通道商户下所有绑定。
@Data
@Accessors(chain = true)
@Schema(title = "支付能力关联应用批量保存参数(商户端)")
public class MchAlipayDirectAppCapabilityBatchParam {

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Valid
    @Schema(description = "支付能力关联应用列表")
    private List<AlipayDirectAppCapabilityItem> items;
}
