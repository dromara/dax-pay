package cn.daxpay.open.channel.ums.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 银联商务直连密钥配置保存参数
@Data
@Accessors(chain = true)
@Schema(title = "银联商务直连密钥配置保存参数")
public class UmsDirectKeyConfigParam {

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "银联商务应用 AppId")
    private String umsAppId;

    @Schema(description = "应用密钥(HmacSHA256 签名密钥)")
    private String appKey;

    @Schema(description = "通讯密钥(回调验签密钥)")
    private String secretKey;
}
