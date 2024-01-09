package cn.bootx.platform.daxpay.service.dto.order.pay;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付订单关联通道信息
 * @author xxm
 * @since 2024/1/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付订单关联通道信息")
public class PayOrderChannelDto extends BaseDto {
}
