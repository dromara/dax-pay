package cn.daxpay.open.channel.llianpay.param.direct;

import cn.daxpay.open.platform.common.json.deserializer.CredentialKeepWhenBlankDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import tools.jackson.databind.annotation.JsonDeserialize;

/// # 连连支付直连密钥配置保存参数
///
/// 以 channelMchNo 定位记录, 仅更新可编辑密钥字段(商户私钥 + 连连平台公钥)与业务参数;
/// mchId 为不可变身份字段, 创建时写入后不参与保存。
@Data
@Accessors(chain = true)
@Schema(title = "连连支付直连密钥配置保存参数")
public class LlianpayDirectKeyConfigParam {

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @NotNull(message = "{validation.field.sandbox.notNull}")
    @Schema(description = "是否沙箱环境")
    private Boolean sandbox;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "商户私钥(Base64 裸串, 兼容 PKCS1/PKCS8)")
    private String merchantPrivateKey;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "连连平台公钥(Base64 裸串, X509 格式)")
    private String llianpayPublicKey;

    @Schema(description = "二级商户号(可空, 平台商模式上送 sub_mchid)")
    private String subMchid;

    @Schema(description = "收款方用户ID(可空, 留空为商户自身收款)")
    private String payeeUid;

    @Schema(description = "业务属性(busi_type, 如 100001 虚拟商品购买)")
    private String busiType;
}
