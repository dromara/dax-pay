package cn.daxpay.open.channel.shouqianba.param.direct;

import cn.daxpay.open.platform.common.json.deserializer.CredentialKeepWhenBlankDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import tools.jackson.databind.annotation.JsonDeserialize;

/// # 收钱吧直连密钥配置保存参数
///
/// 以 channelMchNo 定位记录, 仅更新可编辑密钥字段(终端密钥)与操作员;
/// terminal_sn 为不可变身份字段, 创建时写入后不参与保存。
@Data
@Accessors(chain = true)
@Schema(title = "收钱吧直连密钥配置保存参数")
public class ShouqianbaDirectKeyConfigParam {

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @NotNull(message = "{validation.field.sandbox.notNull}")
    @Schema(description = "是否沙箱环境")
    private Boolean sandbox;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "收钱吧终端密钥(terminal_key, 签到重置后更新)")
    private String terminalKey;

    @Schema(description = "操作员(收钱吧要求上送, 缺省 daxpay)")
    private String operator;
}
