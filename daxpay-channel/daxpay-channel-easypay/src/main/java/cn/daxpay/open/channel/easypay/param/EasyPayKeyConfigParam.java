package cn.daxpay.open.channel.easypay.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 易支付通道密钥配置保存参数
///
/// 以 channelMchNo(通道商户号) 作为唯一标识定位记录,
/// mchNo(平台商户号) 为不可变身份字段, 创建时写入后永不可改, 不参与保存;
/// 密钥字段为空时保留原值(不覆盖)。
@Data
@Accessors(chain = true)
@Schema(title = "易支付通道密钥配置保存参数")
public class EasyPayKeyConfigParam {

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "易支付平台网关地址")
    private String serverUrl;

    @Schema(description = "易支付商户ID(pid)")
    private String partnerId;

    @Schema(description = "商户RSA私钥(PKCS8)")
    private String merchantPrivateKey;

    @Schema(description = "易支付平台验签公钥")
    private String platformPublicKey;
}
