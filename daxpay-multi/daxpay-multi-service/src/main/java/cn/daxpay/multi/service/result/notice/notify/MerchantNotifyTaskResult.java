package cn.daxpay.multi.service.result.notice.notify;

import cn.daxpay.multi.core.result.MchResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 商户订阅通知任务
 * @author xxm
 * @since 2024/8/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "商户订阅通知任务")
public class MerchantNotifyTaskResult extends MchResult {
}
