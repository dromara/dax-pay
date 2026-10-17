package cn.daxpay.open.channel.longpay.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 建行龙支付直连通道商户绑定创建参数
///
/// 创建时录入建行商户三要素(商户号/柜台号/分行号),
/// 两把公钥(柜台公钥/建行公钥)由密钥配置单独维护。
@Data
@Accessors(chain = true)
@Schema(title = "建行龙支付直连通道商户绑定创建参数")
public class LongpayDirectChannelMerchantCreateParam {

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    @NotBlank(message = "{validation.field.channelMerchantName.notBlank}")
    @Schema(description = "通道商户名称")
    private String channelMerchantName;

    @NotBlank(message = "{validation.field.product.notBlank}")
    @Schema(description = "所属支付产品")
    private String product;

    @NotBlank(message = "{validation.field.merchantId.notBlank}")
    @Schema(description = "建行商户号(MERCHANTID)")
    private String merchantId;

    @NotBlank(message = "{validation.field.posId.notBlank}")
    @Schema(description = "建行柜台号(POSID)")
    private String posId;

    @NotBlank(message = "{validation.field.branchId.notBlank}")
    @Schema(description = "建行分行号(BRANCHID)")
    private String branchId;
}
