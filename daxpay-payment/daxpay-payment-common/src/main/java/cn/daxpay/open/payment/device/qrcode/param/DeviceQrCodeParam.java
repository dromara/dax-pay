package cn.daxpay.open.payment.device.qrcode.param;

import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付码牌
@Data
@Accessors(chain = true)
@Schema(title = "支付码牌")
public class DeviceQrCodeParam {

    /// 主键
    @Schema(description = "主键")
    @NotNull(message = "{validation.field.id.notNull}", groups = ValidationGroup.edit.class)
    private Long id;

    /// 码牌名称
    @Schema(description = "码牌名称")
    @NotBlank(message = "{validation.field.qrCodeName.notBlank}")
    @Size(max = 100, message = "{validation.field.qrCodeName.size}")
    private String name;

    /// 金额类型: random-自定义金额 / fixed-固定金额
    /// @see cn.daxpay.open.payment.device.enums.QrCodeAmountTypeEnum
    @Schema(description = "金额类型(random-自定义/fixed-固定)")
    @NotBlank(message = "{validation.field.amountType.notBlank}")
    private String amountType;

    /// 固定金额(分, amount_type=fixed 时必填, 由 Service 层校验)
    @Schema(description = "固定金额(分)")
    private Long fixedAmount;

    /// 状态(enabled/disabled)
    /// @see cn.daxpay.open.payment.device.enums.QrCodeStatusEnum
    @Schema(description = "状态")
    private String status;

    /// 备注
    @Schema(description = "备注")
    @Size(max = 500, message = "{validation.field.remark.size}")
    private String remark;
}
