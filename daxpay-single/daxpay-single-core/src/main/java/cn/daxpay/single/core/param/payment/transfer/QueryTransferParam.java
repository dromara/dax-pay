package cn.daxpay.single.core.param.payment.transfer;

import cn.daxpay.single.core.param.PaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Size;

/**
 * 转账订单查询参数
 * @author xxm
 * @since 2024/5/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "转账订单查询参数")
public class QueryTransferParam extends PaymentCommonParam {

    /** 商户转账号 */
    @Size(max = 100, message = "商户转账号不可超过100位")
    @Schema(description = "商户转账号")
    private String bizTransferNo;

    /** 转账号 */
    @Size(max = 32, message = "转账号不可超过100位")
    @Schema(description = "转账号")
    private String transferNo;
}
