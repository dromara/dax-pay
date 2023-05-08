package cn.bootx.daxpay.param.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author xxm
 * @date 2021/7/21
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付记录查询参数")
public class PaymentQuery implements Serializable {

    private static final long serialVersionUID = 7071042101962400106L;

    @Schema(description = "支付单id")
    private String paymentId;

    @Schema(description = "关联的业务id")
    private String businessId;

    @Schema(description = "关联的业务id")
    private String title;

}
