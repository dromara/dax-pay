package cn.daxpay.open.payment.unipay.result.gateway;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 收银台支付项公开结果
///
/// 落地页列表展示用, 不下发 method/channelMchNo/capability/resolveMode 等路由敏感字段。
@Data
@Accessors(chain = true)
@Schema(title = "收银台支付项(公开)")
public class CashierItemPublicResult {

    @Schema(description = "支付项ID")
    private Long id;

    @Schema(description = "前台展示名称")
    private String name;

    @Schema(description = "图标编码")
    private String icon;

    @Schema(description = "是否推荐")
    private Boolean recommend;

    @Schema(description = "排序号")
    private Integer sortNo;
}
