package cn.daxpay.open.channel.wechat.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 微信直连密钥配置保存参数(商户端)
///
/// 商户端保存/更新微信直连密钥和证书时接收的请求参数。
/// 与运营端 [WechatDirectKeyConfigParam] 的区别: 不含 mchNo, 商户号由后端从登录上下文强制注入, 防越权。
///
@Data
@Accessors(chain = true)
@Schema(title = "微信直连密钥配置保存参数(商户端)")
public class MchWechatDirectKeyConfigParam {

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "API V3密钥")
    private String apiKeyV3;

    @Schema(description = "支付公钥")
    private String publicKey;

    @Schema(description = "支付公钥ID")
    private String publicKeyId;

    @Schema(description = "商户私钥")
    private String privateKey;

    @Schema(description = "商户证书")
    private String privateCert;

    @Schema(description = "证书序列号")
    private String certSerialNo;
}
