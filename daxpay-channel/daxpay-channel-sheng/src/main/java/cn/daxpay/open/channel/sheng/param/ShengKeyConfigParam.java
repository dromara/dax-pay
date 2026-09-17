package cn.daxpay.open.channel.sheng.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 盛付通通道密钥配置保存参数
///
/// 以 channelMchNo(通道商户号) 作为唯一标识定位记录,
/// mchNo(平台商户号) 为不可变身份字段, 创建时写入后永不可改, 不参与保存;
/// 密钥字段为空时保留原值(不覆盖)。
@Data
@Accessors(chain = true)
@Schema(title = "盛付通通道密钥配置保存参数")
public class ShengKeyConfigParam {

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "盛付通商户号(mchId)")
    private String shengMchId;

    @Schema(description = "盛付通分配的 AppId(可空, 直连场景必填)")
    private String sdpAppId;

    @Schema(description = "商户RSA私钥(PKCS8)")
    private String merchantPrivateKey;

    @Schema(description = "盛付通验签公钥")
    private String shengpayPublicKey;
}
