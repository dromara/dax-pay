package cn.daxpay.open.channel.fuyou.param.rsa;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 富友 RSA 通道商户创建参数
@Data
@Accessors(chain = true)
@Schema(title = "富友 RSA 通道商户创建参数")
public class FuyouRsaChannelMerchantCreateParam {

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "平台商户号")
    private String mchNo;

    @NotBlank(message = "{validation.field.product.notBlank}")
    @Schema(description = "支付产品编码(fuyou_internet / fuyou_h5)")
    private String product;

    @Schema(description = "通道商户名称(自定义备注)")
    private String channelMerchantName;

    @NotBlank(message = "{validation.field.fuyouMchNo.notBlank}")
    @Schema(description = "富友商户号(mchnt_cd)")
    private String mchntCd;
}
