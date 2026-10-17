package cn.daxpay.open.channel.umfpay.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 联动优势直连通道商户绑定创建参数
///
/// 创建时录入商户身份(联动优势商户号 mer_id),
/// 商户私钥与平台公钥由密钥配置单独维护。
@Data
@Accessors(chain = true)
@Schema(title = "联动优势直连通道商户绑定创建参数")
public class UmfpayDirectChannelMerchantCreateParam {

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    @NotBlank(message = "{validation.field.channelMerchantName.notBlank}")
    @Schema(description = "通道商户名称")
    private String channelMerchantName;

    @NotBlank(message = "{validation.field.product.notBlank}")
    @Schema(description = "所属支付产品")
    private String product;

    @NotBlank(message = "{validation.field.mchId.notBlank}")
    @Schema(description = "联动优势商户号(mer_id, 创建时录入不可修改)")
    private String merId;
}
