package cn.daxpay.open.payment.masterdata.constants.product.result;

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

    @Schema(description = "产品介绍")
    private String description;

    @Schema(description = "图标")
    private String icon;

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

    @Schema(description = "API调用模式")
    private String apiCallMode;

    @Schema(description = "支付标识类型")
    private String payIdType;

    @Schema(description = "已挂载的支付能力")
    private List<PayProductCapabilityResult> capabilities;
}