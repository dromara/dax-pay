package cn.daxpay.open.payment.unipay.param.trade.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

/// # 订单商品明细
///
/// 对应支付宝 goods_detail 和微信支付 detail.goods_detail，
/// 用于单品营销、电子发票等场景。金额单位为分(Long)，与系统内部全局一致。
@Data
@Schema(title = "订单商品明细")
public class GoodsDetail {

    /// 商户侧商品编码
    /// Alipay → goods_id, WeChat → merchant_goods_id
    @NotBlank(message = "{validation.field.goodsId.notBlank}")
    @Size(max = 64, message = "{validation.field.goodsId.size}")
    @Schema(description = "商户侧商品编码")
    private String goodsId;

    /// 商品名称
    @NotBlank(message = "{validation.field.goodsName.notBlank}")
    @Size(max = 256, message = "{validation.field.goodsName.size}")
    @Schema(description = "商品名称")
    private String goodsName;

    /// 商品数量
    @NotNull(message = "{validation.field.quantity.notNull}")
    @Min(value = 1, message = "{validation.field.quantity.min}")
    @Schema(description = "商品数量")
    private Integer quantity;

    /// 商品单价（分）
    /// Alipay 需转为元，WeChat 直接使用分
    @NotNull(message = "{validation.field.unitPrice.notNull}")
    @DecimalMin(value = "0.01", message = "{validation.field.unitPrice.decimalMin}")
    @Digits(integer = 10, fraction = 2, message = "{validation.field.unitPrice.digits}")
    @Schema(description = "商品单价(分)")
    private BigDecimal unitPrice;

    /// 商品分类（Alipay 独有，选填）
    @Size(max = 32, message = "{validation.field.goodsCategory.size}")
    @Schema(description = "商品分类")
    private String category;

    /// 商品描述（选填）
    @Size(max = 1000, message = "{validation.field.goodsDescription.size}")
    @Schema(description = "商品描述")
    private String description;

    /// 商品展示链接（选填）
    @Size(max = 400, message = "{validation.field.showUrl.size}")
    @Schema(description = "商品展示链接")
    private String showUrl;
}
