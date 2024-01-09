package cn.bootx.platform.daxpay.service.param.record;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付关闭记录
 * @author xxm
 * @since 2024/1/9
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付关闭记录")
public class PayCloseRecordQuery {
}
