package cn.daxpay.open.payment.merchant.result.channel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/// # 通道商户引用明细·按应用分组
///
@Data
@Accessors(chain = true)
@Schema(title = "通道商户引用明细·按应用分组")
public class ChannelMerchantReferenceAppResult {

    /// 应用号
    @Schema(description = "应用号")
    private String appId;

    /// 应用名称
    @Schema(description = "应用名称")
    private String appName;

    /// 商户号
    @Schema(description = "商户号")
    private String mchNo;

    /// 该应用下的引用明细列表
    @Schema(description = "该应用下的引用明细列表")
    private List<ChannelMerchantReferenceItemResult> refs = new ArrayList<>();
}
