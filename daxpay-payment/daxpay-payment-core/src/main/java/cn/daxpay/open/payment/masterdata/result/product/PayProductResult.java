package cn.daxpay.open.payment.masterdata.result.product;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/// # 支付产品
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付产品")
public class PayProductResult extends BaseResult {

    @Schema(description = "产品编码")
    private String code;

    @Schema(description = "产品名称")
    private String name;

    @Schema(description = "关联通道编码")
    private String channel;

    @Schema(description = "通道名称")
    private String channelName;

    @Schema(description = "是否启用")
    private boolean enabled;

    /// 是否已对接上线(true=通道策略已实连; false=规划中, 前端灰显且不提供启停/配置入口)
    private boolean online;

    @Schema(description = "产品介绍")
    private String description;

    @Schema(description = "排序")
    private Integer sortNo;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "是否支持服务商")
    private boolean isv;

    @Schema(description = "是否支持终端报备")
    private boolean terminal;

    @Schema(description = "是否支持沙箱环境")
    private boolean sandbox;

    /// 是否仅支持沙箱环境(模拟通道等): 生效环境恒为 sandbox, 不允许切换到生产
    private boolean sandboxOnly;

    @Schema(description = "API调用模式")
    private String apiCallMode;

    @Schema(description = "支付标识类型")
    private String payIdType;

    /// 当前生效环境(来自支付产品配置, 商户只读): prod/sandbox
    @Schema(description = "当前生效环境: prod/sandbox")
    private String activeEnv;

    @Schema(description = "已挂载的支付能力")
    private List<PayProductCapabilityResult> capabilities;
}