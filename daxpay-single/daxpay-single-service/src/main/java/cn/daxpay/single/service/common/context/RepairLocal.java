package cn.daxpay.single.service.common.context;

import cn.daxpay.single.service.code.PayRepairSourceEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 修复相关信息
 * @author xxm
 * @since 2024/1/25
 */
@Data
@Accessors(chain = true)
public class RepairLocal {

    /**
     * 触发来源
     */
    private PayRepairSourceEnum source;

    /** 完成/退款时间 */
    private LocalDateTime finishTime;

}
