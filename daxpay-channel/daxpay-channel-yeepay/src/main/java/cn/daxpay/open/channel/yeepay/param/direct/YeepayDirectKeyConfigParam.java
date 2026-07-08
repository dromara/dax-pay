package cn.daxpay.open.channel.yeepay.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 易宝直连密钥配置保存参数
///
/// 以 channelMchNo 定位记录, 仅更新可编辑密钥字段;
/// merchantNo/yopIsvNo 为不可变身份字段, 创建时写入后永不可改, 不参与保存。
@Data
@Accessors(chain = true)
@Schema(title = "易宝直连密钥配置保存参数")
public class YeepayDirectKeyConfigParam {

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @NotNull(message = "{validation.field.sandbox.notNull}")
    @Schema(description = "是否沙箱环境")
    private Boolean sandbox;

    @Schema(description = "通道应用 AppKey(YOP 应用标识)")
    private String appKey;

    @Schema(description = "商户 RSA 私钥(PEM PKCS#8)")
    private String privateKey;

    @Schema(description = "易宝平台 RSA 公钥(PEM)")
    private String yopPublicKey;

    @Schema(description = "微信 AppId(微信场景用, 可空)")
    private String wxAppId;

    @Schema(description = "微信 AppSecret(微信场景用, 可空)")
    private String wxAppSecret;
}
