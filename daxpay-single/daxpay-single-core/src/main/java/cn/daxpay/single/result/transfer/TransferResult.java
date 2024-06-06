package cn.daxpay.single.result.transfer;

import cn.daxpay.single.result.PaymentCommonResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 转账结果
 * @author xxm
 * @since 2024/6/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "转账结果")
public class TransferResult extends PaymentCommonResult {
}
