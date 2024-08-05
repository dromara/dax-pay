package cn.daxpay.multi.service.result.notice.callback;

import cn.daxpay.multi.core.result.MchResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 商户回调消息发送记录
 * @author xxm
 * @since 2024/8/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "商户回调消息发送记录")
public class MerchantCallbackRecordResult extends MchResult {
}
