package cn.bootx.platform.daxpay.service.common.context;

import cn.bootx.platform.daxpay.service.code.PayRepairSourceEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付修复上下文
 * @author xxm
 * @since 2024/1/25
 */
@Data
@Accessors(chain = true)
public class RepairLocal {

    /** 触发来源 */
    private PayRepairSourceEnum source;

}
