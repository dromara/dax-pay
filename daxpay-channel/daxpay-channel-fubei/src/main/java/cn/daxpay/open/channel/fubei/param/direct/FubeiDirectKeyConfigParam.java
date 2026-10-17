package cn.daxpay.open.channel.fubei.param.direct;

import cn.daxpay.open.platform.common.json.deserializer.CredentialKeepWhenBlankDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import tools.jackson.databind.annotation.JsonDeserialize;

/// # 付呗直连密钥配置保存参数
///
/// 以 channelMchNo 定位记录, 仅更新可编辑密钥字段(app_secret)与门店ID;
/// appId/merchantId 为不可变身份字段, 创建时写入后不参与保存。
@Data
@Accessors(chain = true)
@Schema(title = "付呗直连密钥配置保存参数")
public class FubeiDirectKeyConfigParam {

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @NotNull(message = "{validation.field.sandbox.notNull}")
    @Schema(description = "是否沙箱环境")
    private Boolean sandbox;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "接口密钥(app_secret, MD5 签名与回调验签共用)")
    private String appSecret;

    @Schema(description = "付呗门店ID(store_id)")
    private String storeId;
}
