package cn.daxpay.open.payment.douyin.param.platform;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 平台抖音应用默认能力绑定批量保存参数
///
/// 按支付产品全量覆盖「支付能力 → 平台应用」绑定：先清该产品再插；items 为空表示清空该产品。
///
@Data
@Accessors(chain = true)
@Schema(title = "平台抖音应用默认能力绑定批量保存参数")
public class DyPlatformAppCapabilityBatchParam {

    @NotBlank(message = "{validation.field.product.notBlank}")
    @Schema(description = "支付产品编码")
    private String product;

    @Valid
    @Schema(description = "能力绑定列表")
    private List<DyPlatformAppCapabilityParam> items;
}
