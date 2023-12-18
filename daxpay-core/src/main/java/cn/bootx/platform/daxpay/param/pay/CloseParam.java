package cn.bootx.platform.daxpay.param.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 支付关闭参数
 * @author xxm
 * @since 2023/12/17
 */
@Data
@Schema(title = "支付关闭参数")
public class CloseParam {

    @Schema(description = "支付单ID")
    private Long paymentId;

    @Schema(description = "业务号")
    private String businessNo;

}
