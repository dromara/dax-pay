package cn.daxpay.single.param.payment.transfer;

import cn.daxpay.single.param.PaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 转账参数
 * @author xxm
 * @since 2024/5/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "转账参数")
public class TransferParam extends PaymentCommonParam {
}
