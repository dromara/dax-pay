package cn.bootx.platform.daxpay.service.param.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付订单查询参数
 * @author xxm
 * @since 2024/1/9
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付订单查询参数")
public class PayOrderQuery {
}
