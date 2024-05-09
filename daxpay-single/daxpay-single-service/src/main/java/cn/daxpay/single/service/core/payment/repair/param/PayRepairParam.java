package cn.daxpay.single.service.core.payment.repair.param;

import cn.daxpay.single.service.code.PayRepairWayEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付订单修复参数
 * @author xxm
 * @since 2023/12/27
 */
@Data
@Accessors(chain = true)
public class PayRepairParam {

    @Schema(description = "修复类型")
    private PayRepairWayEnum repairType;
}
