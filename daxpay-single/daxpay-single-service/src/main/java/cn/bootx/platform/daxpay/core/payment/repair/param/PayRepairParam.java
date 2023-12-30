package cn.bootx.platform.daxpay.core.payment.repair.param;

import cn.bootx.platform.daxpay.code.PayRepairSourceEnum;
import cn.bootx.platform.daxpay.code.PayRepairTypeEnum;
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

    @Schema(description = "修复来源")
    private PayRepairSourceEnum repairSource;

    @Schema(description = "修复类型")
    private PayRepairTypeEnum repairType;

    @Schema(description = "金额")
    private Integer amount;


}
