package cn.daxpay.open.channel.hkrt.result.isv;

import cn.daxpay.open.payment.common.result.MchTradeBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 海科融通通道商户绑定结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "海科融通通道商户绑定结果")
public class HkrtIsvChannelMerchantResult extends MchTradeBaseResult {

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "所属支付产品")
    private String product;

    @Schema(description = "海科商户号")
    private String merchNo;

    @Schema(description = "SAAS 终端号")
    private String pn;
}
