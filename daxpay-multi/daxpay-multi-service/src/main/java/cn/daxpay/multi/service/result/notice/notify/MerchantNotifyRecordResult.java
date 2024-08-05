package cn.daxpay.multi.service.result.notice.notify;

import cn.daxpay.multi.core.result.MchResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 客户订阅通知发送记录
 * @author xxm
 * @since 2024/8/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "客户订阅通知发送记录")
public class MerchantNotifyRecordResult extends MchResult {
}
