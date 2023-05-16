package cn.bootx.platform.daxpay.dto.paymodel.alipay;

import cn.bootx.platform.daxpay.dto.payment.BasePaymentDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author xxm
 * @date 2021/2/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付宝支付记录")
public class AliPaymentDto extends BasePaymentDto implements Serializable {

    private static final long serialVersionUID = 6883103229754466130L;

    @Schema(description = "支付宝交易号")
    private String tradeNo;

}
