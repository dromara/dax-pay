package cn.daxpay.open.channel.leshua.result.isv;

import cn.daxpay.open.payment.common.result.MchTradeBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 乐刷通道商户绑定结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "乐刷通道商户绑定结果")
public class LeshuaIsvChannelMerchantResult extends MchTradeBaseResult {

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "所属支付产品")
    private String product;

    @Schema(description = "乐刷商户编号")
    private String lsMchNo;
}
