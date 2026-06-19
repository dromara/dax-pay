package cn.daxpay.open.payment.channel.param.mch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 通道商户生成参数
///
@Data
@Accessors(chain = true)
@Schema(title = "通道商户生成参数")
public class ChannelMerchantGenParam {

    @Schema(description = "申请单ID")
    private Long applyId;

    @Schema(description = "商户名称")
    private String channelMerchantName;
}
