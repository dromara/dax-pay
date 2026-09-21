package cn.daxpay.open.payment.unipay.param.gateway;

import cn.daxpay.open.payment.unipay.param.MerchantPaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/// # 网关订单查询参数
///
/// 本接口经 [cn.daxpay.open.payment.unipay.aop.PaymentVerify] 验签，切面要求参数为商户身份参数
/// （`instanceof MerchantPaymentCommonParam`）才能装载商户上下文并验签，故必须继承
/// [MerchantPaymentCommonParam]；`mchNo` / `appId` 由父类提供，此处不再重复声明。
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "网关订单查询参数")
public class GatewayOrderQueryParam extends MerchantPaymentCommonParam {

    @Schema(description = "平台网关单号")
    @Size(max = 64, message = "{validation.field.orderNo.size}")
    private String orderNo;

    @Schema(description = "商户业务单号")
    @Size(max = 100, message = "{validation.field.bizOrderNo.size}")
    private String bizOrderNo;
}
