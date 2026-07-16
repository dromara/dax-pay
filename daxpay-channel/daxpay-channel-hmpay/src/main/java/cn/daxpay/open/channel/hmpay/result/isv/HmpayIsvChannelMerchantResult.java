package cn.daxpay.open.channel.hmpay.result.isv;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 河马付通道商户绑定结果
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "河马付通道商户绑定结果")
public class HmpayIsvChannelMerchantResult extends MchBaseResult {

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "所属支付产品")
    private String product;

    @Schema(description = "杉德商户编号")
    private String merchantNo;

    @Schema(description = "门店号")
    private String storeId;
}
