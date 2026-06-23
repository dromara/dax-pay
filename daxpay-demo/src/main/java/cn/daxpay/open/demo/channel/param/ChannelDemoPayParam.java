package cn.daxpay.open.demo.channel.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/// # 通道连通性 Demo 支付参数
///
/// 前端表单提交的简化参数, 不涉及真实支付订单实体。
@Data
@Schema(description = "通道 Demo 支付参数")
public class ChannelDemoPayParam {

    /// 商户订单号
    @Schema(description = "商户订单号")
    @NotBlank(message = "{validation.field.bizOrderNo.notBlank}")
    private String bizOrderNo;

    /// 支付金额(元)
    @Schema(description = "支付金额(元)")
    @NotNull(message = "{validation.field.amount.notNull}")
    @DecimalMin(value = "0.01", message = "{validation.field.amount.min}")
    private BigDecimal amount;

    /// 支付标题
    @Schema(description = "支付标题")
    @NotBlank(message = "{validation.field.subject.notBlank}")
    private String subject;

    /// 支付方式(alipay_wap / alipay_app / alipay_page / alipay_qr)
    @Schema(description = "支付方式", example = "alipay_qr")
    @NotBlank(message = "{validation.field.method.notBlank}")
    private String method;
}
