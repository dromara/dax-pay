package cn.daxpay.open.payment.merchant.param.appinfo;

import cn.daxpay.open.platform.capability.sensitiveword.validation.SensitiveWord;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import cn.daxpay.open.platform.core.enums.merchant.MchAppStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户应用信息
///
@Data
@Accessors(chain = true)
@Schema(title = "商户应用信息")
public class MchAppInfoParam {

    /// 主键
    @Schema(description = "主键")
    @NotNull(message = "{validation.field.id.notNull}", groups = ValidationGroup.edit.class)
    private Long id;

    /// 商户号
    @Schema(description = "商户号")
    private String mchNo;

    /// 应用名称
    @Schema(description = "应用名称")
    @NotNull(message = "{validation.field.appName.notNull}")
    @SensitiveWord
    private String appName;

    /// 应用状态
    /// @see MchAppStatusEnum
    @Schema(description = "应用状态")
    @NotBlank(message = "{validation.field.status.notBlank}")
    private String status;

    /// 默认应用
    @Schema(description = "默认应用")
    private boolean defaultApp;
}

