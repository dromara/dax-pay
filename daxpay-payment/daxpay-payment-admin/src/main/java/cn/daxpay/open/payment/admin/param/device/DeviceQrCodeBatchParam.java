package cn.daxpay.open.payment.admin.param.device;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 批量创建空白码牌参数
@Data
@Accessors(chain = true)
@Schema(title = "批量创建空白码牌参数")
public class DeviceQrCodeBatchParam {

    /// 批次号(全局唯一, 同时作为码牌编码前缀)
    @Schema(description = "批次号")
    @NotBlank(message = "{validation.field.batchNo.notBlank}")
    @Size(max = 64, message = "{validation.field.batchNo.size}")
    private String batchNo;

    /// 创建数量(1-999)
    @Schema(description = "创建数量")
    @NotNull(message = "{validation.field.count.notNull}")
    @Min(value = 1, message = "{validation.field.count.min}")
    @Max(value = 999, message = "{validation.field.count.max}")
    private Integer count;

    /// 码牌名称(可选, 整批统一名称)
    @Schema(description = "码牌名称")
    @Size(max = 100, message = "{validation.field.qrCodeName.size}")
    private String name;

    /// 落地程序类型: h5 / mini_app, 整批统一, 创建后不可改
    /// @see cn.daxpay.open.payment.device.enums.QrCodeProgramTypeEnum
    @Schema(description = "落地程序类型(h5-H5码牌/mini_app-小程序码牌)")
    @NotBlank(message = "{validation.field.programType.notBlank}")
    private String programType;

    /// 金额类型: random-自定义金额 / fixed-固定金额
    /// @see cn.daxpay.open.payment.device.enums.QrCodeAmountTypeEnum
    @Schema(description = "金额类型(random-自定义/fixed-固定)")
    @NotBlank(message = "{validation.field.amountType.notBlank}")
    private String amountType;

    /// 固定金额(分, amount_type=fixed 时必填, 由 Service 层校验)
    @Schema(description = "固定金额(分)")
    private Long fixedAmount;

    /// 状态(enabled/disabled), 空则默认启用
    /// @see cn.daxpay.open.payment.device.enums.QrCodeStatusEnum
    @Schema(description = "状态(enabled-启用/disabled-停用), 空则默认启用")
    private String status;

    /// 备注
    @Schema(description = "备注")
    @Size(max = 500, message = "{validation.field.remark.size}")
    private String remark;
}
