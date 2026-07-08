package cn.daxpay.open.channel.fuyou.result.isv;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 富友通道商户绑定结果
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "富友通道商户绑定")
public class FuyouIsvChannelMerchantResult extends BaseResult {

    @Schema(description = "平台商户号")
    private String mchNo;

    @Schema(description = "通道商户号(FUYOU+雪花)")
    private String channelMchNo;

    @Schema(description = "所属支付产品")
    private String product;

    @Schema(description = "富友商户号(mchnt_cd)")
    private String fuyouMchNo;

    @Schema(description = "终端号(term_id)")
    private String termNo;
}
