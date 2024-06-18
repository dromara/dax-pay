package cn.daxpay.single.core.result.order;

import cn.daxpay.single.core.result.PaymentCommonResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 转账订单数据
 * @author xxm
 * @since 2024/6/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "转账订单数据")
public class TransferOrderResult extends PaymentCommonResult {
}
