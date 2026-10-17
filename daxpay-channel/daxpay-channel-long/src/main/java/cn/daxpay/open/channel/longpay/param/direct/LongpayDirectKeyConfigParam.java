package cn.daxpay.open.channel.longpay.param.direct;

import cn.daxpay.open.platform.common.json.deserializer.CredentialKeepWhenBlankDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import tools.jackson.databind.annotation.JsonDeserialize;

/// # 建行龙支付直连密钥配置保存参数
///
/// 以 channelMchNo 定位记录, 仅更新可编辑密钥字段(两把公钥);
/// 商户三要素(商户号/柜台号/分行号)为不可变身份字段, 创建时写入后不参与保存。
@Data
@Accessors(chain = true)
@Schema(title = "建行龙支付直连密钥配置保存参数")
public class LongpayDirectKeyConfigParam {

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @NotNull(message = "{validation.field.sandbox.notNull}")
    @Schema(description = "是否沙箱环境")
    private Boolean sandbox;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "柜台公钥(请求 MD5 摘要用, 取后 30 位参与 MAC)")
    private String merchantPublicKey;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "建行公钥(异步通知 RSA 验签用, DER hex 或 PEM)")
    private String ccbPublicKey;
}
