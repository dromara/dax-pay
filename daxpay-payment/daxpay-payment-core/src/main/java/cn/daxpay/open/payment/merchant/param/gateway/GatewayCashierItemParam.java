package cn.daxpay.open.payment.merchant.param.gateway;

import cn.daxpay.open.payment.merchant.enums.CashierItemResolveModeEnum;
import cn.daxpay.open.payment.merchant.enums.ClientEnvEnum;
import cn.daxpay.open.payment.merchant.enums.GatewayCashierTypeEnum;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/// # 网关收银台支付项参数
@Data
@Schema(title = "网关收银台支付项参数")
public class GatewayCashierItemParam {

    @Schema(description = "主键")
    @NotNull(message = "{validation.field.id.notNull}", groups = ValidationGroup.edit.class)
    private Long id;

    @Schema(description = "商户号")
    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Size(max = 32, message = "{validation.field.mchNo.size}")
    private String mchNo;

    @Schema(description = "应用号")
    @NotBlank(message = "{validation.field.appId.notBlank}")
    @Size(max = 32, message = "{validation.field.appId.size}")
    private String appId;

    /// @see GatewayCashierTypeEnum
    @Schema(description = "收银台类型: h5/web/mini")
    @NotBlank(message = "{validation.field.cashierType.notBlank}")
    @Size(max = 16, message = "{validation.field.cashierType.size}")
    private String cashierType;

    /// @see ClientEnvEnum
    @Schema(description = "客户端环境(H5五档/MINI四档必填; WEB为空)")
    @Size(max = 32, message = "{validation.field.clientEnv.size}")
    private String clientEnv;

    @Schema(description = "前台展示名称")
    @NotBlank(message = "{validation.field.name.notBlank}")
    @Size(max = 64, message = "{validation.field.itemName.size}")
    private String name;

    @Schema(description = "图标编码")
    @Size(max = 32, message = "{validation.field.icon.size}")
    private String icon;

    @Schema(description = "是否推荐")
    private Boolean recommend;

    @Schema(description = "排序号")
    private Integer sortNo;

    /// @see CashierItemResolveModeEnum
    @Schema(description = "解析模式: method/direct")
    @NotBlank(message = "{validation.field.resolveMode.notBlank}")
    @Size(max = 16, message = "{validation.field.resolveMode.size}")
    private String resolveMode;

    @Schema(description = "支付方式(METHOD模式)")
    @Size(max = 32, message = "{validation.field.method.size}")
    private String method;

    @Schema(description = "通道商户号(DIRECT模式)")
    @Size(max = 64, message = "{validation.field.channelMchNo.size}")
    private String channelMchNo;

    @Schema(description = "支付能力(DIRECT模式)")
    @Size(max = 64, message = "{validation.field.capability.size}")
    private String capability;
}
