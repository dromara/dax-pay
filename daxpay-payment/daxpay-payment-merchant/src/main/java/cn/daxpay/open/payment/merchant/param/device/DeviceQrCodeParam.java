package cn.daxpay.open.payment.merchant.param.device;

import cn.daxpay.open.platform.capability.sensitiveword.validation.SensitiveWord;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付码牌(商户端编辑参数)
///
/// 仅承载业务配置字段; 归属(mchNo/appId/storeNo)走 bind/unbind 系列接口, 编码创建后不可改
@Data
@Accessors(chain = true)
@Schema(title = "支付码牌(商户端)")
public class DeviceQrCodeParam {

    /// 主键
    @Schema(description = "主键")
    @NotNull(message = "{validation.field.id.notNull}", groups = ValidationGroup.edit.class)
    private Long id;

    /// 码牌名称
    @Schema(description = "码牌名称")
    @NotBlank(message = "{validation.field.qrCodeName.notBlank}")
    @Size(max = 100, message = "{validation.field.qrCodeName.size}")
    @SensitiveWord
    private String name;

    /// 金额类型: random-自定义金额 / fixed-固定金额
    /// @see cn.daxpay.open.payment.device.enums.QrCodeAmountTypeEnum
    @Schema(description = "金额类型(random-自定义/fixed-固定)")
    @NotBlank(message = "{validation.field.amountType.notBlank}")
    private String amountType;

    /// 固定金额(分, amount_type=fixed 时必填, 由 Service 层校验)
    @Schema(description = "固定金额(分)")
    private Long fixedAmount;

    /// 是否分账码牌(开启后扫码支付透传分账标识; 产品不支持分账时下单自动降级普通收款)
    @Schema(description = "是否分账码牌")
    private Boolean allocation;

    /// 备注
    @Schema(description = "备注")
    @Size(max = 500, message = "{validation.field.remark.size}")
    private String remark;
}
