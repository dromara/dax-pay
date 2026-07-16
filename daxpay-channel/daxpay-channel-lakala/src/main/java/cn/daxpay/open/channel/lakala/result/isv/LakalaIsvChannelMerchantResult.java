package cn.daxpay.open.channel.lakala.result.isv;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 拉卡拉通道商户绑定结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "拉卡拉通道商户绑定结果")
public class LakalaIsvChannelMerchantResult extends MchBaseResult {

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "所属支付产品")
    private String product;

    @Schema(description = "拉卡拉商户编号")
    private String lakalaMchNo;

    @Schema(description = "终端号")
    private String termNo;
}
