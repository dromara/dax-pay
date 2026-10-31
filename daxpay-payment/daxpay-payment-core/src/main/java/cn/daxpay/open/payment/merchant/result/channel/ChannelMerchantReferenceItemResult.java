package cn.daxpay.open.payment.merchant.result.channel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 通道商户单条引用明细
///
@Data
@Accessors(chain = true)
@Schema(title = "通道商户单条引用明细")
public class ChannelMerchantReferenceItemResult {

    /// 引用类型: route_scene/route_basic/gateway_env/cashier_item
    @Schema(description = "引用类型(route_scene/route_basic/gateway_env/cashier_item)")
    private String type;

    /// 引用类型中文名(通道路由·场景模式 / 通道路由·基础模式 / 网关支付配置 / 收银台支付入口)
    @Schema(description = "引用类型中文名")
    private String typeName;

    /// 人读摘要(如「微信小程序(wechat_mini)」「支付宝环境 × H5」「支付项：支付宝(推荐)」)
    @Schema(description = "引用内容人读摘要")
    private String detail;
}
