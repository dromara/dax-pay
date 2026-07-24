package cn.daxpay.open.payment.wx.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 通道商户微信应用能力绑定批量保存参数
///
/// 按通道商户全量覆盖绑定：先清后插；items 为空表示清空该通道商户下全部绑定。
///
@Data
@Accessors(chain = true)
@Schema(title = "通道商户微信应用能力绑定批量保存参数")
public class WxChannelAppCapabilityBatchParam {

    @Valid
    @Schema(description = "能力绑定列表")
    private List<WxChannelAppCapabilityParam> items;
}
