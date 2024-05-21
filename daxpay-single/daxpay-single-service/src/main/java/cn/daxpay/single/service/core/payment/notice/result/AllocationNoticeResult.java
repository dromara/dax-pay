package cn.daxpay.single.service.core.payment.notice.result;

import cn.daxpay.single.result.PaymentCommonResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 分账通知方法
 * @author xxm
 * @since 2024/5/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "分账通知方法")
public class AllocationNoticeResult extends PaymentCommonResult {

}
