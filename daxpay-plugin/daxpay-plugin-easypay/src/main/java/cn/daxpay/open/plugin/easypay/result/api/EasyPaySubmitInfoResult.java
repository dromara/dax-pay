package cn.daxpay.open.plugin.easypay.result.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/// # 易支付收银台订单信息（内部）
///
@Data
@Accessors(chain = true)
@Schema(title = "易支付收银台信息(内部)")
public class EasyPaySubmitInfoResult {

    /// 协议订单主键
    @Schema(description = "协议订单主键")
    private Long id;

    /// 协议支付方式 alipay/wxpay/aggregate
    @Schema(description = "支付方式")
    private String type;

    /// 商品名称
    @Schema(description = "商品名称")
    private String name;

    /// 金额（元）
    @Schema(description = "金额（元）")
    private BigDecimal money;

    /// 支付链接
    @Schema(description = "支付链接")
    private String payUrl;

    /// 支付参数体
    @Schema(description = "支付参数体")
    private String payBody;

    /// PC 调用方式
    @Schema(description = "PC调用方式")
    private String pcCallType;

    /// 协议状态 0待付 1成功
    @Schema(description = "协议状态 0待付 1成功")
    private Integer status;

    /// 同步跳转地址
    @Schema(description = "同步跳转地址")
    private String returnUrl;
}
