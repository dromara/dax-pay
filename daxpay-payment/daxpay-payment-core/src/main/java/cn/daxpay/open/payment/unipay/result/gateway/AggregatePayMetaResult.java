package cn.daxpay.open.payment.unipay.result.gateway;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 聚合扫码 H5 元数据(公开字段)
///
/// 仅下发前端决策所需信息, 不下发 method/通道商户等敏感路由字段。
@Data
@Accessors(chain = true)
@Schema(title = "聚合扫码元数据")
public class AggregatePayMetaResult {

    /// 是否自动拉起支付
    @Schema(description = "是否自动拉起支付")
    private Boolean autoLaunch;

    /// 当前环境解析出的支付方式是否需要 openId
    @Schema(description = "是否需要先完成 OAuth 获取 openId")
    private Boolean needOpenId;
}
