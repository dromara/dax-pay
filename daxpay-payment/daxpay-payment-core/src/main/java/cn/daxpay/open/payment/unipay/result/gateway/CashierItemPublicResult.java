package cn.daxpay.open.payment.unipay.result.gateway;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 收银台支付项公开结果
///
/// 落地页列表展示用, 不下发 method/channelMchNo/capability/resolveMode 等路由敏感字段。
/// `needOpenId` 由后端按 item.method + clientEnv 综合判定, 控制前端是否跳 OAuth。
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

    @Schema(description = "是否需要先走 OAuth 获取 openId（前端据此跳授权）")
    private Boolean needOpenId;

    // 是否为订单已锁定的支付项（订单支付中且与已锁定的支付方式匹配）
    @Schema(description = "是否为订单已锁定的支付项")
    private Boolean locked;

    // 支付项可用性预检结果: 后端按解析模式做只读路由探测, 路由不可达时 false, 前端应隐藏该项;
    // 默认 true 向后兼容旧前端(不识别该字段的旧前端不受影响)
    @Schema(description = "支付项当前是否可用(路由预检, false 时前端应隐藏)")
    private Boolean available = true;
}
