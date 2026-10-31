package cn.daxpay.open.payment.merchant.result.channel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/// # 通道商户引用明细(按应用分组)
///
@Data
@Accessors(chain = true)
@Schema(title = "通道商户引用明细")
public class ChannelMerchantReferenceResult {

    /// 通道商户号
    @Schema(description = "通道商户号")
    private String channelMchNo;

    /// 总引用处数
    @Schema(description = "总引用处数")
    private Integer total;

    /// 按应用分组的引用明细
    @Schema(description = "按应用分组的引用明细")
    private List<ChannelMerchantReferenceAppResult> apps = new ArrayList<>();
}
