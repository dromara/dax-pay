package cn.daxpay.open.channel.douyin.param.direct;

import cn.daxpay.open.platform.common.json.deserializer.CredentialKeepWhenBlankDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;
import tools.jackson.databind.annotation.JsonDeserialize;

/// # 抖音直连密钥配置保存参数(商户端)
///
/// 商户端保存/更新抖音直连密钥时接收的请求参数。
/// 商户端专用, 不含商户号(防越权: 由控制器从 [cn.daxpay.open.payment.common.context.PaymentContext] 组装),
/// 对照运营端参数 [DouyinDirectKeyConfigParam]。
///
/// 密钥字段留空即保留原值: 空串经 [CredentialKeepWhenBlankDeserializer] 归一为 null,
/// 由保存逻辑的空值不覆盖策略跳过更新。
@Data
@Accessors(chain = true)
@Schema(title = "抖音直连密钥配置保存参数(商户端)")
public class MchDouyinDirectKeyConfigParam {

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "商户私钥")
    private String merchantPrivateKey;

    @Schema(description = "商家公钥证书序列号")
    private String merchantSerialNumber;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "接口加密密钥")
    private String encryptKey;
}
