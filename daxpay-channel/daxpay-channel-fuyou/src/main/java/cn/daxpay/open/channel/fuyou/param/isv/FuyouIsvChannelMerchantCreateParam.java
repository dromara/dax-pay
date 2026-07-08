package cn.daxpay.open.channel.fuyou.param.isv;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 富友通道商户创建参数
@Data
@Accessors(chain = true)
@Schema(title = "富友通道商户创建参数")
public class FuyouIsvChannelMerchantCreateParam {

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "平台商户号")
    private String mchNo;

    @NotBlank(message = "{validation.field.product.notBlank}")
    @Schema(description = "支付产品编码(fuyou_pay)")
    private String product;

    @Schema(description = "通道商户名称(自定义备注)")
    private String channelMerchantName;

    @NotBlank(message = "{validation.field.fuyouMchNo.notBlank}")
    @Schema(description = "富友商户号(mchnt_cd)")
    private String fuyouMchNo;

    @NotBlank(message = "{validation.field.termNo.notBlank}")
    @Schema(description = "终端号(term_id)")
    private String termNo;
}
