package cn.daxpay.open.channel.fuyou.param.direct;

import cn.daxpay.open.platform.common.json.deserializer.CredentialKeepWhenBlankDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;
import tools.jackson.databind.annotation.JsonDeserialize;

/// # 富友直连商户密钥配置保存参数
@Data
@Accessors(chain = true)
@Schema(title = "富友直连商户密钥配置保存参数")
public class FuyouDirectKeyConfigParam {

    @NotBlank(message = "{validation.field.channelMchNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "富友商户号(mchnt_cd)")
    private String mchntCd;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "商户密钥(mchnt_key, 加密存储, 为空时保留原值)")
    private String mchntKey;

    @Schema(description = "终端号(term_id)")
    private String termNo;
}
