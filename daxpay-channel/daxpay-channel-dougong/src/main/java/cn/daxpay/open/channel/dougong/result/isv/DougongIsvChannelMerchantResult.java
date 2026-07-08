package cn.daxpay.open.channel.dougong.result.isv;

import cn.daxpay.open.payment.common.result.MchTradeBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 斗拱通道商户绑定结果
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "斗拱通道商户绑定结果")
public class DougongIsvChannelMerchantResult extends MchTradeBaseResult {

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "所属支付产品")
    private String product;

    @Schema(description = "汇付商户号")
    private String merchantNo;

    @Schema(description = "商户AppId")
    private String appId;
}
