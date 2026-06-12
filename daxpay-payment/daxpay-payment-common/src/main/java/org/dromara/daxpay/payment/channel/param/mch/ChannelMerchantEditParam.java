package org.dromara.daxpay.payment.channel.param.mch;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 通道商户修改参数
///
@Data
@Accessors(chain = true)
@Schema(title = "通道商户修改参数")
public class ChannelMerchantEditParam {

    @NotNull(message = "{validation.field.id.notNull}")
    @Schema(description = "主键")
    private Long id;

    @NotBlank(message = "{validation.field.channelMerchantName.notBlank}")
    @Schema(description = "商户名称")
    private String channelMerchantName;
}
