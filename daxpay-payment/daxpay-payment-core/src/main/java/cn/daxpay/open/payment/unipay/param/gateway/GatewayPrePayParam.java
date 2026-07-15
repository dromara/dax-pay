package cn.daxpay.open.payment.unipay.param.gateway;

import cn.daxpay.open.payment.trade.enums.GatewayPayTypeEnum;
import cn.daxpay.open.payment.unipay.param.MerchantPaymentCommonParam;
import cn.daxpay.open.payment.unipay.param.trade.pay.GoodsDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.OffsetDateTime;
import java.util.List;

/// # 网关预下单参数
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "网关预下单参数")
public class GatewayPrePayParam extends MerchantPaymentCommonParam {

    @Schema(description = "商户订单号")
    @NotBlank(message = "{validation.field.bizOrderNo.notBlank}")
    @Size(max = 100, message = "{validation.field.bizOrderNo.size}")
    private String bizOrderNo;

    @Schema(description = "支付标题")
    @NotBlank(message = "{validation.field.title.notBlank}")
    @Size(max = 100, message = "{validation.field.title.size}")
    private String title;

    @Schema(description = "支付描述")
    @Size(max = 50, message = "{validation.field.description.size}")
    private String description;

    @Schema(description = "支付金额(分)")
    @NotNull(message = "{validation.field.amount.notNull}")
    @Min(value = 1, message = "{validation.field.amount.min}")
    @Max(value = 9999999999L, message = "{validation.field.amount.max}")
    private Long amount;

    /// @see GatewayPayTypeEnum
    @Schema(description = "网关支付类型 cashier/aggregate")
    @NotBlank(message = "{validation.field.gatewayPayType.notBlank}")
    @Size(max = 32, message = "{validation.field.gatewayPayType.size}")
    private String gatewayPayType;

    @Schema(description = "异步通知地址")
    @Size(max = 256, message = "{validation.field.notifyUrl.size}")
    private String notifyUrl;

    @Schema(description = "同步跳转地址")
    @Size(max = 256, message = "{validation.field.returnUrl.size}")
    private String returnUrl;

    @Schema(description = "商户附加参数")
    @Size(max = 512, message = "{validation.field.attach.size}")
    private String attach;

    /// 支付扩展参数（JSON 格式，通道特有的长尾参数；与 [NormalPayParam#extraParam] 对齐）
    @Schema(description = "支付扩展参数")
    @Size(max = 2048, message = "{validation.field.extraParam.size}")
    private String extraParam;

    @Schema(description = "过期时间")
    private OffsetDateTime expiredTime;

    @Schema(description = "商品明细")
    private List<GoodsDetail> goodsDetail;
}
